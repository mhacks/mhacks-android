package com.mhacks.app.mvvm

import androidx.lifecycle.MediatorLiveData
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.RetrofitException
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException

abstract class UseCase<in P, R> {

    protected val disposables = CompositeDisposable()

    protected val resultMediator = MediatorLiveData<Result<R>>()

    open fun observe(): MediatorLiveData<Result<R>> = resultMediator

    protected fun asRetrofitException(throwable: Throwable): RetrofitException? {
        if (throwable is HttpException) {
            val response = throwable.response()
            if (throwable.code() == 401) {
                return RetrofitException.unauthorizedError(
                        response.raw().request().url().toString(), response)
            }
            return RetrofitException.httpError(
                    response.raw().request().url().toString(), response)
        }
        return if (throwable is IOException) {
            RetrofitException.networkError(throwable)
        } else null
    }

    open fun onCleared() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}