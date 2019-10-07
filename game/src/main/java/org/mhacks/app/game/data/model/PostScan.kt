package org.mhacks.app.game.data.model

import com.squareup.moshi.Json

data class PostScan(
        @Json(name="email") var email: String,
        @Json(name="quest") var quest: String)
