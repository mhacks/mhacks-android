package com.mhacks.app.data.models

import androidx.room.*
import com.squareup.moshi.Json

/**
 * Model about the details of the event.
 */
data class ConfigResponse(
    @Json(name = "status") var status: Boolean,
    @Json(name = "user") var user: User?,
    @Json(name = "configuration") var configuration: Configuration
)

@Entity(tableName = "config")
data class Configuration(
    @PrimaryKey
    @Json(name = "id") var id: String,
    @field:Json(name = "app_name") var appName: String,
    @field:Json(name = "start_date") var startDate: String,
    @field:Json(name = "end_date") var endDate: String,
    @Json(name = "createdAt") var createdAt: String,
    @Json(name = "updatedAt") var updatedAt: String,
    @field:Json(name = "is_blackout_page_enabled") var isBlackoutPageEnabled: Boolean,
    @field:Json(name = "is_chat_enabled") var isChatEnabled: Boolean,
    @field:Json(name = "is_application_open") var isApplicationOpen: Boolean,
    @field:Json(name = "is_team_building_enabled") var isTeamBuildingEnabled: Boolean,
    @field:Json(name = "is_live_page_enabled") var isLivePageEnabled: Boolean,
    @Json(name = "deleted") var deleted: Boolean,
    @field:Json(name = "createdAt_ts") var createdAtTs: Long,
    @field:Json(name = "updatedAt_ts") var updatedAtTs: Long,
    @field:Json(name = "start_date_ts") var startDateTs: Long,
    @field:Json(name = "end_date_ts") var endDateTs: Long,
    @field:Json(name = "should_logout") var shouldLogout: Boolean
)