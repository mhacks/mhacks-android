package org.mhacks.app.core.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mhacks.app.core.BuildConfig
import org.mhacks.app.core.data.interceptor.AuthInterceptor
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.di.AuthComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun provideAuthDao(authComponent: AuthComponent) = authComponent.authDao()

    @Provides
    @Singleton
    internal fun provideAuthInterceptor(autoDao: AuthDao) = AuthInterceptor(autoDao)

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache, interceptor: AuthInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG)
            client.addInterceptor(HttpLoggingInterceptor())
        return client.addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient) =
            Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .build()

}