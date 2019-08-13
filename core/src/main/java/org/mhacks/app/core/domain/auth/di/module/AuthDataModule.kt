package org.mhacks.app.core.domain.auth.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.domain.auth.data.AuthDatabase
import org.mhacks.app.core.domain.auth.data.service.AuthService
import org.mhacks.app.core.domain.auth.di.PrivateToAuth
import retrofit2.Retrofit

@Module
class AuthDataModule {

    @Provides
    fun provideAuthDatabase(context: Context) = AuthDatabase.getInstance(context)

    @Provides
    @PrivateToAuth
    fun provideAuthDao(authDatabase: AuthDatabase) = authDatabase.authDao()

    @Provides
    @PrivateToAuth
    fun provideAuthService(retrofit: Retrofit) = retrofit.create(AuthService::class.java)!!

}