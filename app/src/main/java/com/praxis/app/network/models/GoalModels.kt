package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoalNode(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "domain") val domain: String?,
    @Json(name = "progress") val progress: Float,
    @Json(name = "parentId") val parentId: String?,
)

@JsonClass(generateAdapter = true)
data class GoalTreeResponse(
    @Json(name = "nodes") val nodes: List<GoalNode>,
)
