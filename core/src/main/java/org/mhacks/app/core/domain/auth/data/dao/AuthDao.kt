package org.mhacks.app.core.domain.auth.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import org.mhacks.app.core.domain.auth.data.model.Auth

/**
 * Dao for accessing cached user information
 */
@Dao
interface AuthDao {

    @Query("SELECT * FROM auth")
    fun getAuth(): Single<Auth>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(auth: Auth)

}