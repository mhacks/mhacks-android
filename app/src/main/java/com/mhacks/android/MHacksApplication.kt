/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.android

import android.app.Application
import com.mhacks.android.dagger.component.*
import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.AuthModule
import com.mhacks.android.dagger.module.RetrofitModule
import com.mhacks.android.ui.MainActivity
import timber.log.Timber





class MHacksApplication : Application(), MainActivity.OnFromMainActivityCallback {

    private lateinit var netComponent: NetComponent
    lateinit override var hackathonComponent: HackathonComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val appModule = AppModule(this)

        netComponent = DaggerNetComponent.builder()
                .appModule(appModule)
                .authModule(AuthModule(null))
                .retrofitModule(RetrofitModule("https://staging.mhacks.org/v1/"))
                .build()
        hackathonComponent = DaggerHackathonComponent
                .builder()
                .netComponent(netComponent)
                .build()
    }

    override fun setAuthInterceptorToken(token: String) {
        netComponent.authInterceptor.token = token
    }
}