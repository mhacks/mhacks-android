package org.mhacks.app.core.domain.user.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.domain.user.di.PrivateToUser
import org.mhacks.app.core.domain.user.di.UserDatabase
import org.mhacks.app.core.domain.user.service.UserService
import retrofit2.Retrofit

@Module
class UserDataModule {

    @Provides
    internal fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)

    @Provides
    fun provideUserDatabase(context: Context) = UserDatabase.getInstance(context)

    @Provides
    @PrivateToUser
    fun provideUserDao(userDatabase: UserDatabase) = userDatabase.userDao()

}