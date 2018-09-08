package com.mhacks.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mhacks.app.data.Constants
import com.mhacks.app.di.component.*
import com.mhacks.app.di.module.AuthModule
import com.mhacks.app.di.module.RetrofitModule
import com.mhacks.app.di.module.RoomModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class MHacksApplication : DaggerApplication() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this);
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(MHACKS_GROUP,
                    MHACKS_GROUP, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        val url = if (BuildConfig.DEBUG) {
            Constants.STAGING_URL
        } else {
            Constants.RELEASE_URL
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .roomModule(RoomModule())
                .authModule(AuthModule(null))
                .retrofitModule(RetrofitModule(url))
                .build()
        appComponent.inject(this)
        return appComponent
    }

    companion object {
        private const val MHACKS_GROUP = "MHacks Group"
    }
}