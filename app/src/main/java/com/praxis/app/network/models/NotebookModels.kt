package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotebookEntryRequest(
    @Json(name = "entry_type") val entryType: String = "note",
    @Json(name = "title") val title: String? = null,
    @Json(name = "content") val content: String,
    @Json(name = "mood") val mood: String? = null,
    @Json(name = "tags") val tags: List<String>? = null,
    @Json(name = "domain") val domain: String? = null,
    @Json(name = "goal_id") val goalId: String? = null,
    @Json(name = "occurred_at") val occurredAt: String? = null,
)

@JsonClass(generateAdapter = true)
data class NotebookEntryResponse(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String?,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: String,
)
