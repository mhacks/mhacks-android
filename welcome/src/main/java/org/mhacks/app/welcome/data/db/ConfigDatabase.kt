package org.mhacks.app.welcome.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.welcome.data.model.Configuration

@Database(entities = [Configuration::class], version = 1, exportSchema = false)
abstract class ConfigDatabase : RoomDatabase() {

    abstract fun configDao(): ConfigurationDao

    companion object {

        private const val DATABASE_NAME = "config"

        // For Singleton instantiation
        @Volatile private var instance: ConfigDatabase? = null

        fun getInstance(context: Context): ConfigDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ConfigDatabase {
            return Room.databaseBuilder(
                    context, ConfigDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}
