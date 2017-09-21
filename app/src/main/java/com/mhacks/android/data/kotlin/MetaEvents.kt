package com.mhacks.android.data.kotlin

import com.google.gson.annotations.SerializedName

data class MetaEvents(

	@SerializedName("events")
	val events: List<Events>,

	@SerializedName("status")
	val status: Boolean
)

data class Events(

        @SerializedName("endDate_ts")
        val endDateTs: Long,

        @SerializedName("endDate")
        val endDate: String,

        @SerializedName("startDate_ts")
        val startDateTs: Long,

        @SerializedName("createdAt")
        val createdAt: String,

        @SerializedName("deleted")
        val deleted: Boolean,

        @SerializedName("name")
        val name: String,

        @SerializedName("updatedAt_ts")
        val updatedAtTs: Long,

        @SerializedName("location")
        val location: String,

        @SerializedName("id")
        val id: String,

        @SerializedName("category")
        val category: String,

        @SerializedName("createdAt_ts")
        val createdAtTs: Long,

        @SerializedName("startDate")
        val startDate: String,

        @SerializedName("updatedAt")
        val updatedAt: String,

        @SerializedName("desc")
        val desc: String
)