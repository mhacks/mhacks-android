package org.mhacks.app.core.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.domain.auth.AuthRepository
import org.mhacks.app.core.domain.auth.data.di.AuthComponent
import org.mhacks.app.core.domain.user.di.UserComponent
import javax.inject.Singleton

@Module(subcomponents = [AuthComponent::class, UserComponent::class])
class DomainModule {

    @Provides
    @Singleton
    fun authComponent(
            databaseComponentBuilder: AuthComponent.Builder) =
            databaseComponentBuilder.build()

    @Provides
    @Singleton
    fun authRepository(context: Context, authComponent: AuthComponent) =
            AuthRepository(context, authComponent.authDao(), authComponent.authService())

    @Provides
    @Singleton
    fun userDao(
            databaseComponentBuilder: UserComponent.Builder) =
            databaseComponentBuilder.build().userDao()

}
