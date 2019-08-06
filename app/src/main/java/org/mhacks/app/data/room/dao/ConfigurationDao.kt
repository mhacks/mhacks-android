package org.mhacks.app.data.room.dao

import androidx.room.*
import io.reactivex.Single
import org.mhacks.app.data.models.Configuration

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