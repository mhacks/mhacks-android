package com.mhacks.app.di.module

import android.app.Application
import android.content.Context
import com.mhacks.app.di.component.LoginActivityComponent
import com.mhacks.app.di.component.MainActivityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module that exposes the application context.
 */
@Module(subcomponents = [
    MainActivityComponent::class,
    LoginActivityComponent::class
])
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application
}
