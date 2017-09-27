package com.mhacks.app.data.kotlin

/**
 * Created by jeffreychang on 9/20/17.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MetaFloor (
        @SerializedName("status")
        @Expose
        var status: Boolean,
        @SerializedName("floors")
        @Expose
        var floors: List<Floor>)

data class Floor (
        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String,
        @SerializedName("createdAt")
        @Expose
        var createdAt: String,
        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("desc")
        @Expose
        var desc: String,
        @SerializedName("level")
        @Expose
        var level: Int,
        @SerializedName("nw_latitude")
        @Expose
        var nwLatitude: String,
        @SerializedName("nw_longitude")
        @Expose
        var nwLongitude: String,
        @SerializedName("se_latitude")
        @Expose
        var seLatitude: String,
        @SerializedName("se_longitude")
        @Expose
        var seLongitude: String,
        @SerializedName("floor_image")
        @Expose
        var floorImage: String,
        @SerializedName("deleted")
        @Expose
        var deleted: Boolean,
        @SerializedName("createdAt_ts")
        @Expose
        var createdAtTs: Long,
        @SerializedName("updatedAt_ts")
        @Expose
        var updatedAtTs: Long,
        @SerializedName("id")
        @Expose
        var id: String) {

    override fun toString(): String {
        return this.name
    }
}
