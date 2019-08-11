package org.mhacks.app.core.domain.auth.data.di

import dagger.Subcomponent
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.di.module.AuthDataModule
import org.mhacks.app.core.domain.auth.di.PrivateToAuth

@Subcomponent(modules = [AuthDataModule::class])
interface AuthComponent {

    @PrivateToAuth
    fun authDao(): AuthDao

    @Subcomponent.Builder
    interface Builder {

        fun build(): AuthComponent

    }
}