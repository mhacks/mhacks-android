package org.mhacks.app.events.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.events.data.db.EventDatabase
import org.mhacks.app.events.data.service.EventService
import retrofit2.Retrofit

@Module
class EventDataModule {

    @Provides
    @FeatureScope
    fun provideEventService(retrofit: Retrofit): EventService =
            retrofit.create(EventService::class.java)

    @Provides
    @FeatureScope
    fun provideEventDatabase(context: Context) = EventDatabase.getInstance(context)

    @Provides
    @FeatureScope
    fun provideEventDao(eventDatabase: EventDatabase) = eventDatabase.eventDao()

}