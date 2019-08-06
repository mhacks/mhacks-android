package org.mhacks.app.data.models.common

import com.squareup.moshi.Json

data class ErrorResponse(
        @Json(name = "status") var status: Boolean,
        @Json(name = "message") var message: String
)