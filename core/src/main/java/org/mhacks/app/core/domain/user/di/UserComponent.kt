package org.mhacks.app.core.domain.user.di

import dagger.Subcomponent
import org.mhacks.app.core.domain.user.dao.UserDao
import org.mhacks.app.core.domain.user.di.module.UserDataModule

@Subcomponent(modules = [UserDataModule::class])
interface UserComponent {

    @PrivateToUser
    fun userDao(): UserDao

    @Subcomponent.Builder
    interface Builder {

        fun build(): UserComponent

    }
}

