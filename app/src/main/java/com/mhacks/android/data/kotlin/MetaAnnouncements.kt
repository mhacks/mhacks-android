package com.mhacks.android.data.kotlin

import com.google.gson.annotations.SerializedName

data class MetaAnnouncements(

	@SerializedName("announcements")
	val announcements: List<Announcements>,

	@SerializedName("status")
	val status: Boolean
)

data class Announcements(

        @SerializedName("broadcastTime_ts")
        val broadcastTimeTs: Long,

        @SerializedName("isSent")
        val isSent: Boolean,

        @SerializedName("title")
        val title: String,

        @SerializedName("body")
        val body: String,

        @SerializedName("createdAt")
        val createdAt: String,

        @SerializedName("deleted")
        val deleted: Boolean,

        @SerializedName("updatedAt_ts")
        val updatedAtTs: Long,

        @SerializedName("broadcastTime")
        val broadcastTime: String,

        @SerializedName("id")
        val id: String,

        @SerializedName("isApproved")
        val isApproved: Boolean,

        @SerializedName("category")
        val category: String,

        @SerializedName("createdAt_ts")
        val createdAtTs: Long,

        @SerializedName("updatedAt")
        val updatedAt: String
)