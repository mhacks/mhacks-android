package com.mhacks.android.data.model

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

data class Device (
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String,
    @SerializedName("createdAt")
    @Expose
    var createdAt: String,
    @SerializedName("push_id")
    @Expose
    var pushId: String,
    @SerializedName("push_categories")
    @Expose
    var pushCategories: List<String>,
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
    var id: String)

data class FcmDevice (
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("device")
    @Expose
    var device: Device)