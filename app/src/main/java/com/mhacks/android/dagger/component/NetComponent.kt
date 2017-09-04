package com.mhacks.android.dagger.component

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import com.mhacks.android.ui.MainActivity
import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.NetModule
import com.mhacks.android.data.network.NetworkSingleton
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.OkHttpClient





/**
 * Created by jeffreychang on 9/2/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class))
interface NetComponent {

    val retrofit: Retrofit
    fun okHttpClient(): OkHttpClient
    val sharedPreferences: SharedPreferences

    fun inject(activity: Activity)

}