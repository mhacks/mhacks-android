package org.mhacks.app.game.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import org.mhacks.app.game.data.model.GameState

/**
 * Dao for gamestate model.
 */
@Dao
abstract class GameStateDao {

    @Query("SELECT * FROM gameState")
    abstract fun getGameState(): Single<GameState>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateGameState(gamestate: GameState)
}