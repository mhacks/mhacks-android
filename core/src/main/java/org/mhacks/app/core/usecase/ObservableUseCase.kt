package org.mhacks.app.core.usecase

import org.mhacks.app.core.data.model.Outcome
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
                    resultMediator.postValue(Outcome.Success<R>(it))
                }, {
                    asRetrofitException(it)?.let { exception ->
                        resultMediator.postValue(Outcome.Error(exception))
                    } ?: run {
                        resultMediator.postValue(Outcome.Error(it))
                    }

                })
        disposables.add(disposable)
    }
}