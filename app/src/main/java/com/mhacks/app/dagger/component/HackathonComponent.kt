package com.mhacks.app.dagger.component

import com.mhacks.app.dagger.module.AuthModule
import com.mhacks.app.dagger.module.HackathonModule
import com.mhacks.app.dagger.module.RoomModule
import com.mhacks.app.dagger.scope.UserScope
import com.mhacks.app.ui.MainActivity
import com.mhacks.app.ui.login.LoginActivity
import dagger.Component

/**
 * Created by jeffreychang on 9/3/17.
 */
@UserScope
@Component(
        dependencies = arrayOf(NetComponent::class),
        modules = arrayOf(RoomModule::class,
                AuthModule::class,
                HackathonModule::class))
interface HackathonComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: LoginActivity)

}

