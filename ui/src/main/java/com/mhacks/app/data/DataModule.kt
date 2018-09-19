package com.mhacks.app.data

import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.service.*
//import com.mhacks.app.ui.main.usecase.GetAndCacheConfigUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Provides different repositories.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideUserService(retrofit: Retrofit) =
            retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    internal fun provideConfigService(retrofit: Retrofit) =
            retrofit.create(ConfigService::class.java)

    @Provides
    @Singleton
    internal fun provideAnnouncementService(retrofit: Retrofit) =
            retrofit.create(AnnouncementService::class.java)

    @Provides
    @Singleton
    internal fun provideEventService(retrofit: Retrofit) =
            retrofit.create(EventService::class.java)

    @Provides
    @Singleton
    internal fun provideMapService(retrofit: Retrofit) =
            retrofit.create(MapService::class.java)

    @Provides
    @Singleton
    @Named("userRepository")
    fun provideUserRepository(
            userService: UserService,
            loginDao: LoginDao,
            sharedPreferencesManager: SharedPreferencesManager)
            = UserRepository(userService, loginDao, sharedPreferencesManager)

}