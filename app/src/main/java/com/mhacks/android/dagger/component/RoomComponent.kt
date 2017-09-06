package com.mhacks.android.dagger.component

import com.mhacks.android.dagger.module.AppModule
import com.mhacks.android.dagger.module.RoomModule
import com.mhacks.android.data.room.RoomSingleton
import dagger.Component
import javax.inject.Singleton

/**
 * Created by jeffreychang on 9/6/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, RoomModule::class))
interface RoomComponent {

    fun inject(singleton: RoomSingleton)

}