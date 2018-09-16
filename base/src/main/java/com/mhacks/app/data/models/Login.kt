package com.mhacks.app.data.models

/**
 * Model for storing login information.
 */

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "login")
data class Login(
    @PrimaryKey(autoGenerate = false) var id: Int,
    @Json(name = "status") var status: Boolean,
    @Json(name = "message") var message: String,
    @Json(name = "token") var token: String,
    @Ignore @Json(name = "user") var user: User?
) {
    data class Request(
            @Json(name = "email") val email: String,
            @Json(name = "password") val password: String) {

    }
    constructor(id: Int, status: Boolean, message: String, token: String)
            :this(id, status, message, token, null)
}
