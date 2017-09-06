package com.mhacks.android.data.model

/**
 * Created by Omkar Moghe on 10/4/2016.
 */


import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Login(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("token")
    @Expose
    var token: String
)

