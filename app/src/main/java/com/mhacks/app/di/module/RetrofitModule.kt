package com.mhacks.app.di.module

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.mhacks.app.BuildConfig
import com.mhacks.app.data.network.services.MHacksService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


/**
 * Created by jeffreychang on 9/2/17.
 */
@Module class RetrofitModule(
        private var baseUrl: String) {

    @Provides
    @Singleton
    internal fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }


    @Provides
    @Singleton
    internal fun provideOkhttpClient(cache: Cache, interceptor: AuthModule.AuthInterceptor?): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)

        if (BuildConfig.DEBUG)
                client.addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(StethoInterceptor())
        if (interceptor != null)
            client.addInterceptor(interceptor)

        return client.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideMHacksService(retrofit: Retrofit): MHacksService {
        return retrofit.create(MHacksService::class.java)
    }
}