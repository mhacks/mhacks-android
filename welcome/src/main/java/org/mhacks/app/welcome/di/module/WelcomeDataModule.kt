package org.mhacks.app.welcome.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.scope.FeatureScope

import org.mhacks.app.welcome.data.db.ConfigDatabase
import org.mhacks.app.welcome.data.service.ConfigService
import retrofit2.Retrofit

@Module
class WelcomeDataModule {

    @Provides
    @FeatureScope
    fun provideConfigService(retrofit: Retrofit): ConfigService =
            retrofit.create(ConfigService::class.java)

    @Provides
    @FeatureScope
    fun provideConfigDatabase(context: Context) = ConfigDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideConfigDao(configDatabase: ConfigDatabase) = configDatabase.configDao()

}