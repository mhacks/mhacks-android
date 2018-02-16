/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.mhacks.app.dagger.component.*
import com.mhacks.app.dagger.module.AuthModule
import com.mhacks.app.dagger.module.NetModule
import com.mhacks.app.dagger.module.RetrofitModule
import com.mhacks.app.dagger.module.RoomModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class MHacksApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//    val netComponent = .builder()
//            .authModule(AuthModule(null))
//            .retrofitModule(RetrofitModule("http://mhacks.org/"))
//            .build()

        val netModule = NetModule()

        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .netModule(netModule)
                .roomModule(RoomModule())
                .build()
        appComponent.inject(this)
        return appComponent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

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
//    }
//
//    override fun setAuthInterceptorToken(token: String) {
//        netComponent.authInterceptor.token = token
//    }
    }

    companion object {
        private const val MHACKS_GROUP = "MHacks Group"
    }
}