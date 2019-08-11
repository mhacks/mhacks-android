package org.mhacks.app.core.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import timber.log.Timber

class AuthInterceptor(authRepository: AuthDao) : Interceptor {

    private var cachedToken = authRepository
            .getAuth()
            .map { it.token }
            .onErrorReturnItem("")
            .blockingGet()

    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.e("aaasaf")
        if (cachedToken.isNotEmpty()) {
            Timber.e("saf")
            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $cachedToken")
                    .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(chain.request())
    }
}