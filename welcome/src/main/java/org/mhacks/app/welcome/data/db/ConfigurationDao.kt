package org.mhacks.app.welcome.data.db

import androidx.room.*
import io.reactivex.Single
import org.mhacks.app.welcome.data.model.Configuration

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