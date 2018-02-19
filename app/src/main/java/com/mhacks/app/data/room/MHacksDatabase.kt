package com.mhacks.app.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mhacks.app.data.kotlin.*
import com.mhacks.app.data.room.dao.*

/**
 * Declares DAOs for interacting with the SQLite database.
 */

@Database(entities = [
    Login::class,
    User::class,
    Configuration::class,
    Announcement::class,
    Event::class
], version = 3, exportSchema = false)
abstract class MHacksDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao

    abstract fun configDao(): ConfigurationDao

    abstract fun userDao(): UserDao

    abstract fun announcementDao(): AnnouncementDao

    abstract fun eventDao(): EventDao
}