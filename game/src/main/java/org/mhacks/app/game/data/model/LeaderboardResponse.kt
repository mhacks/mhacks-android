package org.mhacks.app.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import org.mhacks.app.game.data.UserInfoTypeConverter

data class LeaderboardResponse(
        @Json(name = "status") var status: Boolean,
        @Json(name = "leaderboard") var leaderboard: List<Player>
)

@TypeConverters(UserInfoTypeConverter::class)
@Entity(tableName = "leaderboard")
data class Player(
        @PrimaryKey
        @Json(name = "user") var user: UserInfo,
        @Json(name = "points") var points: Int
)

data class UserInfo(
        @Json(name = "id") var id: String,
        @Json(name = "full_name") var full_name: String
)