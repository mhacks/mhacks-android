package com.mhacks.app.dagger.module

import com.mhacks.app.dagger.scope.NetScope
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Singleton


/**
 * Created by jeffreychang on 9/10/17.
 */

@Module
class AuthModule(internal var token: String?) {

    @Provides
    @NetScope
    internal fun provideAuthInterceptor(): AuthInterceptor {
        return if (token != null) AuthInterceptor(token!!)
        else AuthInterceptor(null)
    }

    class AuthInterceptor(var token: String?): Interceptor{

        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest: Request = if (token != null) {
                chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build()
            } else {
                chain.request()
            }
            return chain.proceed(newRequest)
        }
    }

}

