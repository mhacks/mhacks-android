package com.mhacks.android.data.model

/**
 * Created by Omkar Moghe on 10/4/2016.
 */


import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mhacks.android.data.kotlin.User

@Entity
data class Login(

    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var userSkipped: Boolean,
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("token")
    @Expose
    var token: String,
    @SerializedName("user")
    @Expose
    @Ignore
    var user: User?) {
    constructor(
            id: Int,
            status: Boolean,
            userSkipped: Boolean,
            message: String,
            token: String
    ):this(id, status, userSkipped, message, token, null)
}

