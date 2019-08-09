package org.mhacks.app.core.data.model

import com.squareup.moshi.Moshi
import retrofit2.Response
import java.io.IOException

class RetrofitException internal constructor(
        message: String?,
        /** The request URL which produced the error.  */
        val url: String?,
        /** Response object containing status code, headers, body, etc.  */
        private val response: Response<*>?,
        /** The event kind which triggered this error.  */
        val kind: Kind,
        val exception: Throwable?) : RuntimeException(message, exception) {


    val code: Int?
        get() {
            return response?.code()
        }

    val errorResponse: ErrorResponse? get() =
        getErrorBodyAs()

    /** Identifies the event kind which triggered a [RetrofitException].  */
    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED,
        /** A 401 HTTP status code was received from the server.  */

        UNAUTHORIZED
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * Returns null if it fails to convert to the object.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun getErrorBodyAs(): ErrorResponse? {
        val moshi = Moshi.Builder()
                .build()

        val adapter = moshi.adapter<ErrorResponse>(ErrorResponse::class.java)
        response?.errorBody()?.let {
            return adapter.fromJson(it.string())
        }
        return null
    }

    companion object {
        fun httpError(url: String, response: Response<*>): RetrofitException {
            val message = """${response.code()} ${response.message()}"""
            return RetrofitException(message, url, response, Kind.HTTP, null)
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message, null, null, Kind.NETWORK, exception)
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(exception.message, null, null, Kind.UNEXPECTED, exception)
        }

        fun unauthorizedError(url: String, response: Response<*>): RetrofitException {
            val message = """${response.code()} ${response.message()}"""
            return RetrofitException(message, url, response, Kind.UNAUTHORIZED, null)
        }
    }
}