package com.mhacks.app.data.room.dao


import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
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