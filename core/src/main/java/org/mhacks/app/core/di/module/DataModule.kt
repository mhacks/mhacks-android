package org.mhacks.app.core.di.module

import android.content.Context
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
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
    fun provideHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideAuthDao(authComponent: AuthComponent) = authComponent.authDao()

    @Provides
    @Singleton
    fun provideAuthInterceptor(autoDao: AuthDao) = AuthInterceptor(autoDao)


    @Provides
    @Singleton
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin = NetworkFlipperPlugin()

    @Provides
    @Singleton
    fun provideOkHttpClient(
            cache: Cache,
            interceptor: AuthInterceptor,
            networkFlipperPlugin: NetworkFlipperPlugin
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG)
            client.addInterceptor(
                    FlipperOkhttpInterceptor(networkFlipperPlugin)
            )
        return client.addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .build()

}