package org.mhacks.app.events.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import org.mhacks.app.data.model.Event

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