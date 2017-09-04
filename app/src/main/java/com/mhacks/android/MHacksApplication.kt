/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.android

import android.app.Application
import com.mhacks.android.dagger.component.DaggerHackathonComponent
import com.mhacks.android.dagger.component.DaggerNetComponent
import com.mhacks.android.dagger.component.HackathonComponent
import com.mhacks.android.dagger.component.NetComponent
import com.mhacks.android.dagger.module.NetModule
import com.mhacks.android.dagger.module.AppModule



class MHacksApplication : Application() {

    lateinit var hackathonComponent: HackathonComponent
    lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule("https://staging.mhacks.org/v1/"))
                .build()
        hackathonComponent = DaggerHackathonComponent
                .builder()
                .netComponent(netComponent)
                .build()
    }
}