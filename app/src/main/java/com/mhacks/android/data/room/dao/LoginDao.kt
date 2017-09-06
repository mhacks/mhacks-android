package com.mhacks.android.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mhacks.android.data.model.Login
import io.reactivex.Single

/**
 * Created by jeffreychang on 9/6/17.
 */
@Dao
interface LoginDao {

    @Query("SELECT * FROM login")
    fun getLogin(): Single<Login>

    @Insert
    fun insertLogin(login: Login)
}