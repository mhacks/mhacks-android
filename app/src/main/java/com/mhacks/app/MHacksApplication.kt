/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.mhacks.app.dagger.component.*
import com.mhacks.app.dagger.module.AppModule
import com.mhacks.app.dagger.module.AuthModule
import com.mhacks.app.dagger.module.RetrofitModule
import com.mhacks.app.ui.MainActivity
import timber.log.Timber

class MHacksApplication : Application(), MainActivity.OnFromMainActivityCallback {

    private lateinit var netComponent: NetComponent
    lateinit override var hackathonComponent: HackathonComponent
    private val mhacksGroup = "MHacks Group"

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val appModule = AppModule(this)

        netComponent = DaggerNetComponent.builder()
                .appModule(appModule)
                .authModule(AuthModule(null))
                .retrofitModule(RetrofitModule("https://mhacks.org/v1/"))
                .build()
        hackathonComponent = DaggerHackathonComponent
                .builder()
                .netComponent(netComponent)
                .build()

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(mhacksGroup,
                    mhacksGroup, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    override fun setAuthInterceptorToken(token: String) {
        netComponent.authInterceptor.token = token
    }
}