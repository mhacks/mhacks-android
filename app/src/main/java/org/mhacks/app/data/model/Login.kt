package org.mhacks.app.data.model

/**
 * Model for storing login information.
 */

import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "login")
data class Login(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "login_id") var id: Int,
    @Json(name = "status") var status: Boolean,
    @Json(name = "message") var message: String,
    @Json(name = "token") var token: String,
    @Embedded @Json(name = "user") var user: User?
) {

    // Checks if user or groups are in the user currently null.
    // If so, it will return false. The only case it will return true is if "admin is in groups.
    val isAdmin get() =  if (user != null) {
        user!!.isAdmin
    } else false

    val isSkipped get() = user == null

    data class Request(
            @Json(name = "email") val email: String,
            @Json(name = "password") val password: String) {
    }

    @Ignore constructor(id: Int, status: Boolean, message: String, token: String)
            :this(id, status, message, token, null)
}
