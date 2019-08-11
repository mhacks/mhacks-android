package org.mhacks.app.core.domain.user.di

import dagger.Subcomponent
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.service.AuthService
import org.mhacks.app.core.domain.auth.di.PrivateToAuth
import org.mhacks.app.core.domain.user.UserRepository
import org.mhacks.app.core.domain.user.dao.UserDao
import org.mhacks.app.core.domain.user.di.module.UserDataModule
import org.mhacks.app.core.domain.user.service.UserService

@Subcomponent(modules = [UserDataModule::class])
interface UserComponent {

    @PrivateToUser
    fun userDao(): UserDao

    @PrivateToUser
    fun userService(): UserService

    @Subcomponent.Builder
    interface Builder {

        fun build(): UserComponent

    }
}

