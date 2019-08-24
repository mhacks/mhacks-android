package org.mhacks.app.announcements.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.announcements.data.model.Announcement

@Database(entities = [Announcement::class], version = 1, exportSchema = false)
abstract class AnnouncementDatabase : RoomDatabase() {

    abstract fun announcementDao(): AnnouncementDao

    companion object {

        private const val DATABASE_NAME = "announcement"

        // For Singleton instantiation
        @Volatile private var instance: AnnouncementDatabase? = null

        fun getInstance(context: Context): AnnouncementDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AnnouncementDatabase {
            return Room.databaseBuilder(
                    context, AnnouncementDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}
