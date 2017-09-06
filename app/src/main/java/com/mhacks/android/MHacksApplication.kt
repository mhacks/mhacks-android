/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.android

import android.app.Application
import com.mhacks.android.dagger.component.*
import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.RetrofitModule
import timber.log.Timber





class MHacksApplication : Application() {

    private lateinit var netComponent: NetComponent
    lateinit var hackathonComponent: HackathonComponent
    lateinit var roomComponent: RoomComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .retrofitModule(RetrofitModule("https://staging.mhacks.org/v1/"))
                .build()
        hackathonComponent = DaggerHackathonComponent
                .builder()
                .netComponent(netComponent)
                .build()
        roomComponent = DaggerRoomComponent.create()
    }
}