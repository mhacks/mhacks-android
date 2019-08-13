package org.mhacks.app.core.domain.auth.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.core.domain.auth.data.dao.AuthDao
import org.mhacks.app.core.domain.auth.data.model.Auth

@Database(entities = [Auth::class], version = 1, exportSchema = false)
abstract class AuthDatabase : RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object {

        private const val DATABASE_NAME = "auth"

        // For Singleton instantiation
        @Volatile private var instance: AuthDatabase? = null

        fun getInstance(context: Context): AuthDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AuthDatabase {
            return Room.databaseBuilder(
                    context, AuthDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}
