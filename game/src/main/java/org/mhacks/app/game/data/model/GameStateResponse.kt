package org.mhacks.app.game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.squareup.moshi.Json
import org.mhacks.app.game.data.GameStateConverter

data class GameStateResponse(
        @Json(name = "status") var status: Boolean,
        @Json(name = "state") var state: GameState
)

@TypeConverters(GameStateConverter::class)
@Entity(tableName = "gameState")
data class GameState(
        @Json(name = "updatedAt") var updatedAt: String,
        @Json(name = "createdAt") var createdAt: String,
        @Json(name = "answers") var answers: Map<String, String>,
        @Json(name = "completedQuests") var completedQuests: List<Quest>,
        @Json(name = "quests") var quests: List<Quest>,
        @Json(name = "scans") var scans: List<String>,
        @Json(name = "points") var points: Int,
        @Json(name = "user") var user: String,
        @Json(name = "deleted") var deleted: Boolean,
        @Json(name = "createdAt_ts") var createdAtTs: Long,
        @Json(name = "updatedAt_ts") var updatedAtTs: Long,
        @PrimaryKey
        @Json(name = "id") var id: String
)

data class Quest(
        @Json(name = "question") var question: String,
        @Json(name = "answer") var answer: String,
        @Json(name = "points") var points: Int,
        @Json(name = "requiredScans") var requiredScans: Int,
        @Json(name = "scans") var scans: List<String>,
        @Json(name = "questionText") var questionText: String?
)
