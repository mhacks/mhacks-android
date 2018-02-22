package com.mhacks.app.data.models
import com.squareup.moshi.Json


/**
 * Model for response for verifying an user.
 */
data class VerifyTicketResponse(
    @Json(name = "status") var status: Boolean,
    @Json(name = "feedback") var feedback: List<Feedback>
)

data class Feedback(
    @Json(name = "label") var label: String,
    @Json(name = "value") var value: String
)