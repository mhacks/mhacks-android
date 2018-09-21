package com.mhacks.app.mvvm

import com.mhacks.app.data.models.Result
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<in P, R>: UseCase<P, R>() {

    abstract fun getSingle(parameters: P): Single<R>

    fun execute(parameters: P) {
        val disposable = getSingle(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    resultMediator.postValue(Result.Success<R>(it))
                }, {
                    asRetrofitException(it)?.let { exception ->
                        resultMediator.postValue(Result.Error(exception))
                    } ?: run {
                        resultMediator.postValue(Result.Error(it))
                    }
                })
        disposables.add(disposable)
    }
}