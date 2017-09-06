package com.mhacks.android.data.room

import com.mhacks.android.MHacksApplication
import com.mhacks.android.data.network.NetworkSingleton

/**
 * Created by jeffreychang on 9/6/17.
 */
class RoomSingleton private constructor(application: MHacksApplication) {

    init {
        application.roomComponent.inject(this)
    }

    companion object {
        fun newInstance(application: MHacksApplication): NetworkSingleton {
            return NetworkSingleton(application = application)
        }
    }
}