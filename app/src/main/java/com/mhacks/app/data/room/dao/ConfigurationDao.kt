package com.mhacks.app.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mhacks.app.data.kotlin.Configuration
import io.reactivex.Single

/**
 * Dao for the configuration object.
 */
@Dao
interface ConfigurationDao {

    @Query("SELECT * FROM config")
    fun getConfig(): Single<Configuration>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfig(config: Configuration)
}