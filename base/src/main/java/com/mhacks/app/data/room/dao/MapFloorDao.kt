package com.mhacks.app.data.room.dao

import android.arch.persistence.room.*
import com.mhacks.app.data.models.MapFloor
import io.reactivex.Single

/**
 * Dao for the map floor model.
 */
@Dao
abstract class MapFloorDao {

    @Query("SELECT * FROM mapFloor")
    abstract fun getFloors(): Single<List<MapFloor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateMapFloors(announcements: List<MapFloor>)

    @Query("DELETE FROM mapFloor")
    abstract fun deleteMapFloors()

    @Transaction
    open fun deleteAndUpdateMapFloors(mapFloors: List<MapFloor>) {
        deleteMapFloors()
        updateMapFloors(mapFloors)
    }
}