package org.mhacks.app.core.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.domain.auth.data.di.AuthComponent
import org.mhacks.app.core.domain.user.di.UserComponent
import javax.inject.Singleton

@Module(subcomponents = [AuthComponent::class, UserComponent::class])
class DomainModule {

    @Provides
    @Singleton
    fun authDao(
            databaseComponentBuilder: AuthComponent.Builder) =
            databaseComponentBuilder.build().authDao()

    @Provides
    @Singleton
    fun userDao(
            databaseComponentBuilder: UserComponent.Builder) =
            databaseComponentBuilder.build().userDao()

}
