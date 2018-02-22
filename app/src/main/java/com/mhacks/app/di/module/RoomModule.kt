package com.mhacks.app.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.mhacks.app.data.room.MHacksDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module that provides the room database.
 */
@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideMHacksDatabase(application: Application): MHacksDatabase =
        Room.databaseBuilder(application, MHacksDatabase::class.java, "mhacks-db")
                .fallbackToDestructiveMigration()
                .build()
}