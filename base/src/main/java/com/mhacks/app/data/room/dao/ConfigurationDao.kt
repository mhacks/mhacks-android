package com.mhacks.app.data.room.dao

import androidx.room.*
import com.mhacks.app.data.models.Configuration
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