package org.mhacks.app.ui.main.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.core.domain.user.service.UserService
import retrofit2.Retrofit

@Module
class MainDataModule {

    @Provides
    @FeatureScope
    fun provideUserService(retrofit: Retrofit) : UserService =
            retrofit.create(UserService::class.java)

}