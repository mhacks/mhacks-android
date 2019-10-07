package org.mhacks.app.game.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.game.data.model.Player

@Database(entities = [Player::class], version = 1, exportSchema = false)
abstract class LeaderboardDatabase : RoomDatabase() {

    abstract fun leaderboardDao(): LeaderboardDao

    companion object {

        private const val DATABASE_NAME = "leaderboard"

        // For Singleton instantiation
        @Volatile private var instance: LeaderboardDatabase? = null

        fun getInstance(context: Context): LeaderboardDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): LeaderboardDatabase {
            return Room.databaseBuilder(
                    context, LeaderboardDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}


