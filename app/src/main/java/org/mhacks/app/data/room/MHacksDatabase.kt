package org.mhacks.app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mhacks.app.data.models.Announcement
import org.mhacks.app.data.models.Configuration
import org.mhacks.app.data.models.Event
import org.mhacks.app.data.models.Login
import org.mhacks.app.data.models.MapFloor
import org.mhacks.app.data.models.User
import org.mhacks.app.data.room.dao.AnnouncementDao
import org.mhacks.app.data.room.dao.ConfigurationDao
import org.mhacks.app.data.room.dao.EventDao
import org.mhacks.app.data.room.dao.LoginDao
import org.mhacks.app.data.room.dao.MapFloorDao
import org.mhacks.app.data.room.dao.UserDao

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