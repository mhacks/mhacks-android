package com.mhacks.app.mvvm

import com.mhacks.app.data.models.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<in P, R>: UseCase<P, R>() {

    abstract fun getObservable(parameters: P): Observable<R>

    fun execute(parameters: P) {
        val disposable = getObservable(parameters)
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