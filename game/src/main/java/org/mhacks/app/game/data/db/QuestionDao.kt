package org.mhacks.app.game.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import org.mhacks.app.game.data.model.Question

/**
 * Dao for question model.
 */
@Dao
abstract class QuestionDao {

    @Query("SELECT * FROM questions")
    abstract fun getQuestions(): Single<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateQuestions(questions: List<Question>)
}

