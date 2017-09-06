package com.mhacks.android.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.room.dao.LoginDao

/**
 * Created by jeffreychang on 9/6/17.
 */

@Database(entities = arrayOf(Login::class), version = 1, exportSchema = false)
abstract class MHacksDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
}