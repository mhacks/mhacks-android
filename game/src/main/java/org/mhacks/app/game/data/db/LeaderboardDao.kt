package org.mhacks.app.game.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import org.mhacks.app.game.data.model.Player

/**
 * Dao for leaderboard model.
 */
@Dao
abstract class LeaderboardDao {

    @Query("SELECT * FROM leaderboard")
    abstract fun getLeaderboard(): Single<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateLeaderboard(leaderboard: List<Player>)
}