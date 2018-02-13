package com.mhacks.app.dagger.component

import com.mhacks.app.dagger.module.AuthModule
import com.mhacks.app.dagger.module.RetrofitModule
import dagger.Component

/**
 * Created by jeffreychang on 9/2/17.
 */

@Component(modules = [AuthModule::class, RetrofitModule::class])
interface NetComponent {

    @Component.Builder
    interface Builder {

        fun authModule(authModule: AuthModule): Builder

        fun retrofitModule(retrofitModule: RetrofitModule): Builder

        fun build(): NetComponent
    }
}