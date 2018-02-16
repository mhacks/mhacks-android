package com.mhacks.app.dagger.component

import com.mhacks.app.MHacksApplication
import com.mhacks.app.dagger.module.AuthModule
import com.mhacks.app.dagger.module.RetrofitModule
import com.mhacks.app.dagger.scope.NetScope
import dagger.Subcomponent
import retrofit2.Retrofit

/**
 * Created by jeffreychang on 9/2/17.
 */

@NetScope
@Subcomponent(
        modules = [AuthModule::class, RetrofitModule::class])
interface NetComponent {

    @Subcomponent.Builder
    interface Builder {

        fun retrofitModule(retrofitModule: RetrofitModule): Builder

        fun authModule(authModule: AuthModule): Builder

        fun build(): NetComponent
    }
}