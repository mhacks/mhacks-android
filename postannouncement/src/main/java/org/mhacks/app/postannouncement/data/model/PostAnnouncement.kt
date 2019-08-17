package org.mhacks.app.postannouncement.data.model

import com.squareup.moshi.Json

/**
 * Model used for creating an announcement.
 */
data class PostAnnouncement(
        @Json(name="title") var title: String,
        @Json(name="body") var body: String,
        @Json(name="category") var category: String,
        @Json(name="isApproved") var isApproved: Boolean,
        @Json(name="isSent") var isSent: Boolean,
        @Json(name="push") var push: Boolean)