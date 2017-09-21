package com.mhacks.android.dagger.component

import android.app.Activity
import android.app.Application
import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.AuthInterceptor
import com.mhacks.android.dagger.module.AuthModule
import com.mhacks.android.dagger.module.RetrofitModule
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by jeffreychang on 9/2/17.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, AuthModule::class, RetrofitModule::class))
interface NetComponent {

    val app: Application
    val retrofit: Retrofit
    val authInterceptor: AuthInterceptor

    fun inject(activity: Activity)

}