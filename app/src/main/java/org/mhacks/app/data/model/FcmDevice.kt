package org.mhacks.app.data.model

import com.squareup.moshi.Json

/**
 * Model that is necessary to send to Firebase Cloud Messaging services to receive push
 * notifications
 */
data class Device (
    @Json(name="updatedAt") var updatedAt: String,
    @Json(name="createdAt") var createdAt: String,
    @Json(name="push_id") var pushId: String,
    @Json(name="push_categories") var pushCategories: List<String>,
    @Json(name="deleted") var deleted: Boolean,
    @Json(name="createdAt_ts") var createdAtTs: Long,
    @Json(name="updatedAt_ts") var updatedAtTs: Long,
    @Json(name="id") var id: String)

data class FcmDevice (
    @Json(name="status") var status: Boolean,
    @Json(name="device") var device: Device)
