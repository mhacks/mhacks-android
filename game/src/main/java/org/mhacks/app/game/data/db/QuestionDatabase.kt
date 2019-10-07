package org.mhacks.app.game.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.mhacks.app.game.data.model.Question

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {

        private const val DATABASE_NAME = "questions"

        // For Singleton instantiation
        @Volatile private var instance: QuestionDatabase? = null

        fun getInstance(context: Context): QuestionDatabase {
            return instance ?: synchronized(this) {
                instance
                        ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): QuestionDatabase {
            return Room.databaseBuilder(
                    context, QuestionDatabase::class.java,
                    DATABASE_NAME
            ).build()
        }
    }
}


