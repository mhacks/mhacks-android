package org.mhacks.app.core.domain.auth.data.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.domain.auth.data.AuthDatabase
import org.mhacks.app.core.domain.auth.di.PrivateToAuth

@Module
class AuthDataModule {

    @Provides
    fun provideAuthDatabase(context: Context) = AuthDatabase.getInstance(context)

    @Provides
    @PrivateToAuth
    fun provideAuthDao(authDatabase: AuthDatabase) = authDatabase.authDao()

}