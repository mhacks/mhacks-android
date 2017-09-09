package com.mhacks.android.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mhacks.android.data.kotlin.RoomUser
import io.reactivex.Single

/**
 * Created by jeffreychang on 9/6/17.
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Single<RoomUser>

    @Insert
    fun insertUser(user: RoomUser)
}