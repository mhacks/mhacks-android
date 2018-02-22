package com.mhacks.app.di.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.mhacks.app.data.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Module that provides shared preferences.
 */
@Module
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences)
            : SharedPreferencesManager =
            SharedPreferencesManager(sharedPreferences)
}