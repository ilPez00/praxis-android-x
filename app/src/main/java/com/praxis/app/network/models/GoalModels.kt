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
    @Json(name = "customDetails") val customDetails: String? = null,
    @Json(name = "targetDate") val targetDate: String? = null,
    @Json(name = "completionMetric") val completionMetric: String? = null,
    @Json(name = "weight") val weight: Float? = null,
    @Json(name = "children") val children: List<GoalNode>? = null,
    @Json(name = "created_at") val createdAt: String? = null,
)

@JsonClass(generateAdapter = true)
data class GoalTreeResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "user_id") val userId: String?,
    @Json(name = "nodes") val nodes: List<GoalNode>,
    @Json(name = "root_nodes") val rootNodes: List<String>?,
    @Json(name = "domain_proficiency") val domainProficiency: Map<String, Double>?,
    @Json(name = "created_at") val createdAt: String?,
    @Json(name = "updated_at") val updatedAt: String?,
)

@JsonClass(generateAdapter = true)
data class CreateGoalNodeRequest(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "domain") val domain: String? = null,
    @Json(name = "targetDate") val targetDate: String? = null,
    @Json(name = "completionMetric") val completionMetric: String? = null,
    @Json(name = "parentId") val parentId: String? = null,
)

@JsonClass(generateAdapter = true)
data class CreateGoalNodeResponse(
    @Json(name = "node") val node: GoalNode,
    @Json(name = "newBalance") val newBalance: Int,
)

@JsonClass(generateAdapter = true)
data class UpdateGoalNodeRequest(
    @Json(name = "name") val name: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "domain") val domain: String? = null,
    @Json(name = "targetDate") val targetDate: String? = null,
    @Json(name = "completionMetric") val completionMetric: String? = null,
    @Json(name = "prerequisiteGoalIds") val prerequisiteGoalIds: List<String>? = null,
)

@JsonClass(generateAdapter = true)
data class UpdateGoalNodeResponse(
    @Json(name = "node") val node: GoalNode,
    @Json(name = "newBalance") val newBalance: Int,
)

@JsonClass(generateAdapter = true)
data class ProgressUpdateRequest(
    @Json(name = "progress") val progress: Int,
)

@JsonClass(generateAdapter = true)
data class ProgressUpdateResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "nodeId") val nodeId: String,
    @Json(name = "progress") val progress: Int,
)

@JsonClass(generateAdapter = true)
data class DeleteGoalNodeResponse(
    @Json(name = "message") val message: String,
    @Json(name = "deletedCount") val deletedCount: Int,
    @Json(name = "newBalance") val newBalance: Int,
)
