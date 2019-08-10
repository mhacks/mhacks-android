package org.mhacks.app.maps.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.data.model.MapFloor

@Database(entities = [MapFloor::class], version = 1, exportSchema = false)
abstract class MapDatabase : RoomDatabase() {

    abstract fun mapDao(): MapFloorDao

    companion object {

        private const val DATABASE_NAME = "mapFloor"

        // For Singleton instantiation
        @Volatile private var instance: MapDatabase? = null

        fun getInstance(context: Context): MapDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MapDatabase {
            return Room.databaseBuilder(
                    context, MapDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}
