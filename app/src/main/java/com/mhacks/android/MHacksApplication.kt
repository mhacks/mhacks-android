/**
 * Created by jeffreychang on 9/2/17.
 */

package com.mhacks.android

import android.app.Application
import com.mhacks.android.dagger.component.DaggerNetComponent
import com.mhacks.android.dagger.component.HackathonComponent
import com.mhacks.android.dagger.component.NetComponent
import com.mhacks.android.dagger.module.NetModule
import com.mhacks.android.dagger.module.AppModule



class MHacksApplication : Application() {
    private lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule("http://www.jsonplaceholder.typicode.com/"))
                .build()
//        hackathonComponent = DaggerH

    }



    companion object {
        val netComponent: NetComponent
            get() = netComponent
    }
}
