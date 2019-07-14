package com.mhacks.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mhacks.app.data.network.fcm.RegistrationIntentService
import com.mhacks.app.data.network.services.FireBaseService
import com.mhacks.app.di.component.*
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.di.module.RetrofitModule
import com.mhacks.app.di.module.RoomModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class MHacksApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    private val mhacksService = retrofit.create(FireBaseService::class.java)

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(MHACKS_GROUP,
                    MHACKS_GROUP, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.argb(255, 203, 20, 88)
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val pushToken = sharedPreferences.getString(RegistrationIntentService.PUSH_TOKEN, "")
        if (pushToken.length > 0) {
            val authToken = sharedPreferences.getString(RegistrationIntentService.AUTH_TOKEN, "")
            mhacksService.postFireBaseToken(pushToken, "Bearer ${authToken}")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { sharedPreferences.edit().putBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, true).apply() },
                            { Timber.e(it) }
                    )
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .roomModule(RoomModule())
                .authModule(AuthModule(null))
                .retrofitModule(RetrofitModule(BuildConfig.API_URL))
                .build()
        appComponent.inject(this)
        return appComponent
    }

    companion object {
        private const val MHACKS_GROUP = "MHacks Group"
    }
}
