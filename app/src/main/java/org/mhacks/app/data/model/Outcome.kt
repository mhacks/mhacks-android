package org.mhacks.app.data.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Outcome<out R> {

    data class Success<out T>(val data: T) : Outcome<T>()

    data class Error<out R: Throwable>(val exception: R) : Outcome<Nothing>()

    object Loading : Outcome<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Outcome] is of type [Outcome.Success] & holds non-null [Outcome.Success.data].
 */
val Outcome<*>.succeeded
    get() = this is Outcome.Success && data != null
