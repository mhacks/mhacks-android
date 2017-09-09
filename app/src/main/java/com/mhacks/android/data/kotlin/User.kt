package com.mhacks.android.data.kotlin

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jeffreychang on 9/6/17.
 */

@Entity
data class UserResponse (
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("user")
    @Expose
    var user: User
)

@Entity(tableName = "user")
data class RoomUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var fullName: String,
    var university: String
)

data class User (

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("email_verified")
    @Expose
    var emailVerified: Boolean,

    @SerializedName("application_submitted")
    @Expose
    var applicationSubmitted: Boolean,

    @SerializedName("full_name")
    @Expose
    var fullName: String,

    @SerializedName("groups")
    @Expose
    @Ignore
    var groups: List<String>,

    @SerializedName("major")
    @Expose
    var major: String,

    @SerializedName("university")
    @Expose
    var university: String,

    @SerializedName("resume_uploaded")
    @Expose
    var resumeUploaded: Boolean,

    @SerializedName("avatar")
    @Expose
    @Ignore
    var avatar: List<String>,

    @SerializedName("github")
    @Expose
    var github: String,

    @SerializedName("linkedin")
    @Expose
    var linkedin: String,

    @SerializedName("devspost")
    @Expose
    var devpost: String,

    @SerializedName("portfolio")
    @Expose
    var portfolio: String,

    @SerializedName("tshirt")
    @Expose
    var tshirt: String,

    @SerializedName("race")
    @Expose
    var race: String,

    @SerializedName("sex")
    @Expose
    var sex: String
)