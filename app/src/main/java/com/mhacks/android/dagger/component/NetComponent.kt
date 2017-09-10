package com.mhacks.android.dagger.component

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.RetrofitModule
import com.mhacks.android.dagger.module.RoomModule
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by jeffreychang on 9/2/17.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, RetrofitModule::class))
interface NetComponent {

    val app: Application

    val retrofit: Retrofit
    fun okHttpClient(): OkHttpClient

    fun inject(activity: Activity)

}