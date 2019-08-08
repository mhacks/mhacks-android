package org.mhacks.app.events.di.module

import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.data.room.MHacksDatabase
import org.mhacks.app.data.room.dao.EventDao
import org.mhacks.app.events.data.service.EventService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class EventDataModule {

    @Provides
    @FeatureScope
    fun provideEventService(retrofit: Retrofit): EventService =
            retrofit.create(EventService::class.java)

    @Provides
    @FeatureScope
    fun provideEventDao(mHacksDatabase: MHacksDatabase) = mHacksDatabase.eventDao()

}