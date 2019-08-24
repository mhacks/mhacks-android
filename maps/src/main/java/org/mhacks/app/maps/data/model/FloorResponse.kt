package org.mhacks.app.maps.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * Model for floors of the buildings to be overlay on Google Maps.
 */
data class FloorResponse(
		@Json(name = "status") var status: Boolean,
		@Json(name = "floors") var floors: List<MapFloor>
)

@Entity(tableName = "mapFloor")
data class MapFloor(
		@Json(name = "updatedAt") var updatedAt: String,
		@Json(name = "createdAt") var createdAt: String,
		@Json(name = "name") var name: String,
		@Json(name = "desc") var desc: String,
		@Json(name = "level") var level: Int,
		@field:Json(name = "nw_latitude") var nwLatitude: String,
		@field:Json(name = "nw_longitude") var nwLongitude: String,
		@field:Json(name = "se_latitude") var seLatitude: String,
		@field:Json(name = "se_longitude") var seLongitude: String,
		@field:Json(name = "floor_image") var floorImage: String,
		@Json(name = "deleted") var deleted: Boolean,
		@field:Json(name = "createdAt_ts") var createdAtTs: Long,
		@field:Json(name = "updatedAt_ts") var updatedAtTs: Long,
        @PrimaryKey
		@Json(name = "id") var id: String
)