package com.mhacks.app.data.room.dao

import android.arch.persistence.room.*
import com.mhacks.app.data.kotlin.Floor
import io.reactivex.Single

/**
 * Dao for the map floor model.
 */
@Dao
abstract class MapFloorDao {

    @Query("SELECT * FROM mapFloor")
    abstract fun getFloors(): Single<List<Floor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateMapFloors(announcements: List<Floor>)

    @Query("DELETE FROM mapFloor")
    abstract fun deleteMapFloors()

    @Transaction
    open fun deleteAndUpdateMapFloors(announcements: List<Floor>) {
        deleteMapFloors()
        updateMapFloors(announcements)
    }
}