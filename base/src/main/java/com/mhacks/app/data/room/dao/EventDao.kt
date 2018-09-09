package com.mhacks.app.data.room.dao

import android.arch.persistence.room.*
import com.mhacks.app.data.models.Event
import io.reactivex.Single

/**
 * Dao for event model.
*/
@Dao
abstract class EventDao {

    @Query("SELECT * FROM event")
    abstract fun getEvents(): Single<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateEvents(announcements: List<Event>)

    @Query("DELETE FROM event")
    abstract fun deleteEvents()

    @Transaction
    open fun deleteAndUpdateEvents(events: List<Event>) {
        deleteEvents()
        updateEvents(events)
    }
}