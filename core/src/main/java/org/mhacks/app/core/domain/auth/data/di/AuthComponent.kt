package org.mhacks.app.core.domain.auth.data.di

import dagger.Subcomponent
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.service.AuthService
import org.mhacks.app.core.domain.auth.di.PrivateToAuth
import org.mhacks.app.core.domain.auth.di.module.AuthDataModule

@Subcomponent(modules = [AuthDataModule::class])
abstract class AuthComponent {

    @PrivateToAuth
    abstract fun authDao(): AuthDao

    @PrivateToAuth
    abstract fun authService(): AuthService

    @Subcomponent.Builder
    interface Builder {

        fun build(): AuthComponent

    }
}