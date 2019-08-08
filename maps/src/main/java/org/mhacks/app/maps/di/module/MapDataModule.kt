package org.mhacks.app.maps.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.maps.data.service.MapService
import retrofit2.Retrofit

@Module
class MapDataModule {

    @Provides
    @FeatureScope
    fun provideMapService(retrofit: Retrofit) : MapService =
            retrofit.create(MapService::class.java)
}