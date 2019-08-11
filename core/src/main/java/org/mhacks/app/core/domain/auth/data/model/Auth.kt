package org.mhacks.app.core.domain.auth.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.mhacks.app.core.domain.user.data.User

/**
 * Model for storing auth information.
 */
@Entity(tableName = "auth")
data class Auth(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "login_id") var id: Int,
        @Json(name = "status") var status: Boolean,
        @Json(name = "message") var message: String,
        @Json(name = "token") var token: String,
        @Embedded @Json(name = "user") var user: User?
) {

    val isSkipped get() = user == null

    // Checks if user or groups are in the user currently null.
    // If so, it will return false. The only case it will return true is if "admin is in groups.
    val isAdmin
        get() = if (user != null) {
            user!!.isAdmin
        } else false

    @Ignore
    constructor(id: Int, status: Boolean, message: String, token: String)
            : this(id, status, message, token, null)

    companion object {

        fun empty() = Auth(1, false, "", "")

    }
}
