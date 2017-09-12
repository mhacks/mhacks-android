package com.mhacks.android.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mhacks.android.data.kotlin.Configuration
import com.mhacks.android.data.kotlin.RoomUser
import com.mhacks.android.data.kotlin.User
import com.mhacks.android.data.model.Login
import com.mhacks.android.data.room.dao.ConfigurationDao
import com.mhacks.android.data.room.dao.LoginDao
import com.mhacks.android.data.room.dao.UserDao

/**
 * Created by jeffreychang on 9/6/17.
 */

@Database(entities = arrayOf(
        Login::class,
        User::class,
        Configuration::class), version = 2, exportSchema = false)
abstract class MHacksDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao
    abstract fun userDao(): UserDao
    abstract fun configDao(): ConfigurationDao
}