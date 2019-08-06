package org.mhacks.app.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import org.mhacks.app.data.models.Login

/**
 * Created by jeffreychang on 9/6/17.
 */
@Dao
interface LoginDao {

    @Query("SELECT * FROM login")
    fun getLogin(): Single<Login>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLogin(login: Login)

}