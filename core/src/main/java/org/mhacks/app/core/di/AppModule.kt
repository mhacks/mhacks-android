package org.mhacks.app.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.ThemePrefProvider
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)!!

    @Provides
    @Singleton
    fun provideThemePrefProvider(sharedPreferences: SharedPreferences) =
            ThemePrefProvider(sharedPreferences)
}
