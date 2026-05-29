package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrackerLogRequest(
    @Json(name = "tracker_id") val trackerId: String,
    @Json(name = "data") val data: Map<String, Any>,
    @Json(name = "logged_at") val loggedAt: String? = null,
)

@JsonClass(generateAdapter = true)
data class TrackerDef(
    @Json(name = "id") val id: String,
    @Json(name = "type") val type: String,
    @Json(name = "goal") val goal: String?,
)
