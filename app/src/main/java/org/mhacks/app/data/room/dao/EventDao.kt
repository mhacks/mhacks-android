package org.mhacks.app.data.room.dao

import androidx.room.*
import io.reactivex.Single
import org.mhacks.app.data.models.Event

/**
 * Dao for event model.
*/
@Dao
abstract class EventDao {

    @Query("SELECT * FROM event")
    abstract fun getEvents(): Single<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateEvents(events: List<Event>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateEvent(event: Event)

    @Query("DELETE FROM event")
    abstract fun deleteEvents()

    @Transaction
    open fun deleteAndUpdateEvents(events: List<Event>) {
        deleteEvents()
        updateEvents(events)
    }
}