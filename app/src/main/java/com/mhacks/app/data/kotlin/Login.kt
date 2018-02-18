package com.mhacks.app.data.kotlin

/**
 * Created by Omkar Moghe on 10/4/2016.
 */

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "login")
data class Login(
        @PrimaryKey(autoGenerate = false) var id: Int,
		@Json(name = "status") var status: Boolean,
		@Json(name = "message") var message: String,
		@Json(name = "token") var token: String
)
