package com.mhacks.android.dagger.component

import com.mhacks.android.dagger.module.HackathonModule
import com.mhacks.android.dagger.module.RoomModule
import com.mhacks.android.dagger.scope.UserScope
import com.mhacks.android.ui.MainActivity
import dagger.Component

/**
 * Created by jeffreychang on 9/3/17.
 */
@UserScope
@Component(dependencies = arrayOf(NetComponent::class), modules = arrayOf(RoomModule::class ,HackathonModule::class))
interface HackathonComponent {
    fun inject(activity: MainActivity)
}