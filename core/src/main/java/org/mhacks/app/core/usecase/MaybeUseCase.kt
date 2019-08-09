package org.mhacks.app.core.usecase

import io.reactivex.Maybe
import io.reactivex.Scheduler
import org.mhacks.app.core.data.model.Outcome

abstract class MaybeUseCase<in P, R>: UseCase<P, R>() {

    abstract val threadExecutor: Scheduler

    abstract val postExecuteExecutor: Scheduler

    abstract fun getMaybe(parameters: P): Maybe<R>

    fun execute(parameters: P) {
        val disposable = getMaybe(parameters)
                .subscribeOn(threadExecutor)
                .observeOn(postExecuteExecutor)
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
}