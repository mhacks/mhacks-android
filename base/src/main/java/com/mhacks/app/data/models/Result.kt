package com.mhacks.app.data.models

import com.mhacks.app.data.models.common.RetrofitException
import java.net.UnknownHostException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error<out R: Throwable>(val exception: R) : Result<Nothing>() {

        val kind: Kind get() {
            return when (exception) {
                is UnknownHostException -> {
                    Kind.NETWORK
                } else -> {
                    Kind.UNKNOWN
                }
            }
        }

        enum class Kind {
            NETWORK,
            UNKNOWN
        }
    }

    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Result.Success] & holds non-null [Result.Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null
