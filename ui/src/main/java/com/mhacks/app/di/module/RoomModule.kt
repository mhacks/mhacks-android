package com.mhacks.app.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.mhacks.app.data.room.MHacksDatabase
import com.mhacks.app.data.room.dao.LoginDao
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

    @Singleton
    @Provides
    fun provideLoginDao(mHacksDatabase: MHacksDatabase) = mHacksDatabase.loginDao()

    @Singleton
    @Provides
    fun provideConfigDao(mHacksDatabase: MHacksDatabase) =
            mHacksDatabase.configDao()

    @Singleton
    @Provides
    fun provideEventDao(mHacksDatabase: MHacksDatabase) =
            mHacksDatabase.eventDao()

    @Singleton
    @Provides
    fun provideMapFloorDao(mHacksDatabase: MHacksDatabase) =
            mHacksDatabase.mapFloorDao()

    @Singleton
    @Provides
    fun provideAnnouncementDao(mHacksDatabase: MHacksDatabase) =
            mHacksDatabase.announcementDao()

    @Singleton
    @Provides
    fun provideUserdao(mHacksDatabase: MHacksDatabase) =
            mHacksDatabase.userDao()

}