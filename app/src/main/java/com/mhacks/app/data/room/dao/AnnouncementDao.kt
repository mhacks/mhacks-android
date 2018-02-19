package com.mhacks.app.data.room.dao

import android.arch.persistence.room.*
import com.mhacks.app.data.kotlin.Announcement
import io.reactivex.Single

/**
 * Dao for Announcement model.
 */

@Dao
abstract class AnnouncementDao {

    @Query("SELECT * FROM announcement")
    abstract fun getAnnouncements(): Single<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateAnnouncements(announcements: List<Announcement>)

    @Query("DELETE FROM announcement")
    abstract fun deleteAnnouncements()

    @Transaction
    open fun deleteAndUpdateAnnouncements(announcements: List<Announcement>) {
        deleteAnnouncements()
        updateAnnouncements(announcements)
    }
}