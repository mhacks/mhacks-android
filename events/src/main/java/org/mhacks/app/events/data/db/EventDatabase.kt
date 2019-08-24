package org.mhacks.app.events.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.events.data.model.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {

        private const val DATABASE_NAME = "event"

        // For Singleton instantiation
        @Volatile private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EventDatabase {
            return Room.databaseBuilder(
                    context, EventDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}
