package org.mhacks.app.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import org.mhacks.app.data.model.User

/**
 * Created by jeffreychang on 9/6/17.
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}