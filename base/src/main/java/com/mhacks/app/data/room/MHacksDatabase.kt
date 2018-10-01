package com.mhacks.app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhacks.app.data.models.*
import com.mhacks.app.data.room.dao.*

/**
 * Declares DAOs for interacting with the SQLite database.
 */

@Database(entities = [
    Login::class,
    User::class,
    Configuration::class,
    Announcement::class,
    Event::class,
    MapFloor::class
], version = 4, exportSchema = false)
abstract class MHacksDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao

    abstract fun configDao(): ConfigurationDao

    abstract fun userDao(): UserDao

    abstract fun announcementDao(): AnnouncementDao

    abstract fun eventDao(): EventDao

    abstract fun mapFloorDao(): MapFloorDao
}