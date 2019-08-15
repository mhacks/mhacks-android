package org.mhacks.app.core.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.mhacks.app.core.domain.auth.data.dao.AuthDao

class AuthInterceptor(private val authDao: AuthDao) : Interceptor {

    private var cachedToken = ""
        get() {
            if (field.isBlank()) {
                field = loadToken()
            }
            return field
        }

    private fun loadToken() = authDao
            .getAuth()
            .map { it.token }
            .onErrorReturnItem("")
            .blockingGet()

    override fun intercept(chain: Interceptor.Chain): Response {
        if (cachedToken.isNotEmpty()) {
            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $cachedToken")
                    .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(chain.request())
    }
}