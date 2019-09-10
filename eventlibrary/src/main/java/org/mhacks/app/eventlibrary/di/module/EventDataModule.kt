package org.mhacks.app.eventlibrary.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import org.mhacks.app.eventlibrary.data.db.EventDatabase
import org.mhacks.app.eventlibrary.data.service.EventService
import retrofit2.Retrofit

@Module
class EventDataModule {

    @Provides
    fun provideEventService(retrofit: Retrofit): EventService =
            retrofit.create(EventService::class.java)

    @Provides
    fun provideEventDatabase(context: Context) = EventDatabase.getInstance(context)

    @Provides
    fun provideEventDao(eventDatabase: EventDatabase) = eventDatabase.eventDao()

}