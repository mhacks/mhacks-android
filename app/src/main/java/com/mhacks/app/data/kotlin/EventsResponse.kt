package com.mhacks.app.data.kotlin

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json

data class EventsResponse(
	@Json(name = "status") var status: Boolean,
	@Json(name = "events") var events: List<Event>
)

@Entity(tableName = "event")
data class Event(
	@PrimaryKey
	@Json(name = "id") var id: String,
	@Json(name = "updatedAt") var updatedAt: String,
	@Json(name = "createdAt") var createdAt: String,
	@Json(name = "name") var name: String,
	@Json(name = "desc") var desc: String,
	@Json(name = "startDate") var startDate: String,
	@Json(name = "endDate") var endDate: String,
	@Json(name = "location") var location: String,
	@Json(name = "category") var category: String,
	@Json(name = "deleted") var deleted: Boolean,
	@field:Json(name = "createdAt_ts") var createdAtTs: Long,
	@field:Json(name = "updatedAt_ts") var updatedAtTs: Long,
	@field:Json(name = "startDate_ts") var startDateTs: Long,
	@field:Json(name = "endDate_ts") var endDateTs: Long
)