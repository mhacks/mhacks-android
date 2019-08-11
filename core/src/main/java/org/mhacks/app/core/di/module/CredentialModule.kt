package org.mhacks.app.core.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.data.interceptor.AuthInterceptor
import org.mhacks.app.core.domain.user.UserRepository

import javax.inject.Singleton

//@Module
//class CredentialModule(private var token: String?) {
//
//    @Provides
//    @Singleton
//    fun provideAuthInterceptor(userRepository: UserRepository) {
//        AuthInterceptor(userRepository)
//    }
//
//}
//
