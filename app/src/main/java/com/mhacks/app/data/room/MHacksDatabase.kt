package com.mhacks.app.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mhacks.app.data.kotlin.Configuration
import com.mhacks.app.data.kotlin.User
import com.mhacks.app.data.model.Login
import com.mhacks.app.data.room.dao.ConfigurationDao
import com.mhacks.app.data.room.dao.LoginDao
import com.mhacks.app.data.room.dao.UserDao

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