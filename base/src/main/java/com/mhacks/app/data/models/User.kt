package com.mhacks.app.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mhacks.app.data.room.Converters
import com.squareup.moshi.Json

/**
 * Model for storing information about an user's information.
 */

data class UserResponse(
		@Json(name = "status") var status: Boolean?,
		@Json(name = "user") var user: User
)

@TypeConverters(Converters::class)
@Entity(tableName = "user")
data class User(
    @PrimaryKey
	@ColumnInfo(name = "user_id")
    @Json(name = "id") var id: String,
    @Json(name = "createdAt") var createdAt: String?,
    @Json(name = "updatedAt") var updatedAt: String?,
    @Json(name = "deleted") var deleted: Boolean?,
    @field:Json(name = "full_name") var fullName: String?,
    @Json(name = "email") var email: String?,
    @Json(name = "email_verified") var emailVerified: Boolean?,
    @Json(name = "application_submitted") var applicationSubmitted: Boolean?,
    @Json(name = "password") var password: String?,
    @Json(name = "created_at_ts") var createdAtTs: Int?,
    @Json(name = "birthday_ts") var birthdayTs: Int?,
    @Json(name = "birthday") var birthday: String?,
    @Json(name = "major") var major: String?,
    @field:Json(name = "university") var university: String?,
	@Json(name="avatar") var avatar: String?,
    @Json(name = "resume") var resume: String?,
    @Json(name = "github") var github: String?,
	@Json(name = "groups") var groups: List<String>?,
    @Json(name = "linkedin") var linkedin: String?,
    @Json(name = "devpost") var devpost: String?,
    @Json(name = "portfolio") var portfolio: String?,
    @Json(name = "tshirt") var tshirt: String?,
    @Json(name = "race") var race: String?,
    @Json(name = "sex") var sex: String?
) {
	val isAdmin
		get() = if (groups == null) {
			false
		} else {
			groups?.any {
				it == "admin"
			}
		}
}
