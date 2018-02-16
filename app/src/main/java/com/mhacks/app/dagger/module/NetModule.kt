package com.mhacks.app.dagger.module

import com.mhacks.app.dagger.component.NetComponent
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by jeffreychang on 2/15/18.
 */

@Module(subcomponents = [NetComponent::class])
class NetModule {

//    @Provides
//    @Singleton
//    fun provideRetrofit(retrofit: Retrofit): Retrofit = retrofit
}