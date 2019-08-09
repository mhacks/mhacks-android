package org.mhacks.app.core.data.model

import com.squareup.moshi.Json

data class ErrorResponse(
        @Json(name = "status") var status: Boolean,
        @Json(name = "message") var message: String
)