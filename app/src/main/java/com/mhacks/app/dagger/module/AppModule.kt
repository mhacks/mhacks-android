package com.mhacks.app.dagger.module

import android.app.Application
import android.content.Context
import com.mhacks.app.dagger.component.LoginActivityComponent
import com.mhacks.app.dagger.component.MainActivityComponent
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
