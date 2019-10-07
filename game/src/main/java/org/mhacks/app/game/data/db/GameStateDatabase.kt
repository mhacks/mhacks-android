package org.mhacks.app.game.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.game.data.model.GameState

@Database(entities = [GameState::class], version = 1, exportSchema = false)
abstract class GameStateDatabase : RoomDatabase() {

    abstract fun gameStateDao(): GameStateDao

    companion object {

        private const val DATABASE_NAME = "gameState"

        // For Singleton instantiation
        @Volatile private var instance: GameStateDatabase? = null

        fun getInstance(context: Context): GameStateDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GameStateDatabase {
            return Room.databaseBuilder(
                    context, GameStateDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}


