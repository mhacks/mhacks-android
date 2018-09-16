package com.mhacks.app.di

import android.arch.lifecycle.MediatorLiveData
import com.mhacks.app.data.models.Result
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class UseCase<in P, R> {

    private val disposables = CompositeDisposable()

    private val result = MediatorLiveData<Result<R>>()

    abstract fun getSingle(parameters: P): Single<R>

    fun execute(parameters: P) {
        val disposable = getSingle(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result.postValue(Result.Success<R>(it))
                }, {
                    Timber.e(it)
                    result.postValue(Result.Error(it))
                })
        disposables.add(disposable)
    }

    open fun observe(): MediatorLiveData<Result<R>> {
        return result
    }

    fun onCleared() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

}