package org.mhacks.app.core.usecase

import io.reactivex.Single
import org.mhacks.app.core.DispatcherProvider
import org.mhacks.app.core.data.model.Outcome
import javax.inject.Inject

abstract class SingleUseCase<in P, R>: UseCase<P, R>() {

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    abstract fun getSingle(parameters: P): Single<R>

    fun execute(parameters: P) {
        val disposable = getSingle(parameters)
                .subscribeOn(dispatcherProvider.io)
                .observeOn(dispatcherProvider.main)
                .subscribe({
                    resultMediator.postValue(Outcome.Success(it))
                }, {
                    asRetrofitException(it)?.let { exception ->
                        resultMediator.postValue(Outcome.Error(exception))
                    } ?: run {
                        resultMediator.postValue(Outcome.Error(it))
                    }
                })
        disposables.add(disposable)
    }

    operator fun invoke(parameters: P) = execute(parameters)

}