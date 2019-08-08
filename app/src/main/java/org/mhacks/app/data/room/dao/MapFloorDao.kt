package org.mhacks.app.data.room.dao

import androidx.room.*
import io.reactivex.Single
import org.mhacks.app.data.model.MapFloor

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