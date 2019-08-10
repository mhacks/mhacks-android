package org.mhacks.app.maps.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.maps.data.db.MapDatabase
import org.mhacks.app.maps.data.service.GetImageFromUrlService
import org.mhacks.app.maps.data.service.MapService
import retrofit2.Retrofit

@Module
class MapDataModule {

    @Provides
    @FeatureScope
    fun provideMapService(retrofit: Retrofit) : MapService =
            retrofit.create(MapService::class.java)

    @Provides
    @FeatureScope
    fun provideGetImageFromUrlService() : GetImageFromUrlService = GetImageFromUrlService()

    @Provides
    @FeatureScope
    fun provideMapDatabase(context: Context) = MapDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideMapDao(mapDatabase: MapDatabase) = mapDatabase.mapDao()

}