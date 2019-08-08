package org.mhacks.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * Model about announcements.
 */
data class AnnouncementResponse(
    @Json(name = "status") var status: Boolean,
    @Json(name = "announcements") var announcements: List<Announcement>
)

@Entity(tableName = "announcement")
data class Announcement(
        @Json(name = "updatedAt") var updatedAt: String,
        @Json(name = "createdAt") var createdAt: String,
        @Json(name = "title") var title: String,
        @Json(name = "body") var body: String,
        @Json(name = "isSent") var isSent: Boolean,
        @Json(name = "isApproved") var isApproved: Boolean,
        @Json(name = "category") var category: String,
        @Json(name = "broadcastTime") var broadcastTime: String,
        @Json(name = "deleted") var deleted: Boolean,
        @field:Json(name = "createdAt_ts") var createdAtTs: Long,
        @field:Json(name = "updatedAt_ts") var updatedAtTs: Long,
        @field:Json(name = "broadcastTime_ts") var broadcastTimeTs: Long,
        @PrimaryKey
        @Json(name = "id") var id: String
)