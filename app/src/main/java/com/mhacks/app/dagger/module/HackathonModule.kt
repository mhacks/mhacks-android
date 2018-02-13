package com.mhacks.app.dagger.module

import com.mhacks.app.dagger.scope.UserScope
import com.mhacks.app.data.network.services.HackathonApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by jeffreychang on 9/3/17.
 */

@Module class HackathonModule {

    @Provides
    @UserScope
    internal fun provideHackathonApiInterface(retrofit: Retrofit): HackathonApiService {
        return retrofit.create(HackathonApiService::class.java)
    }
}