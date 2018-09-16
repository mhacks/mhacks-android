package com.mhacks.app.data

import com.mhacks.app.data.repository.UserRepository
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.service.ConfigService
import com.mhacks.app.data.service.UserService
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
    @Named("userRepository")
    fun provideUserRepository(
            userService: UserService,
            loginDao: LoginDao,
            sharedPreferencesManager: SharedPreferencesManager)
            = UserRepository(userService, loginDao, sharedPreferencesManager)

//    @Provides
//    @Singleton
//    internal fun provideCheckLoginAuthUseCase(
//            userRepository: UserRepository) =
//            CheckLoginAuthUseCase(userRepository)
//
//    @Provides
//    @Singleton
//    internal fun provideCheckAdminAuthUserCase(
//            userRepository: UserRepository) =
//            GetAndCacheConfigUseCase(userRepository)

}