package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DashboardSummaryResponse(
    @Json(name = "goalTree") val goalTree: GoalTreeResponse?,
    @Json(name = "activeBets") val activeBets: List<ActiveBet>,
    @Json(name = "checkedIn") val checkedIn: Boolean,
    @Json(name = "briefs") val briefs: List<DailyBrief>,
    @Json(name = "todayBrief") val todayBrief: DailyBrief?,
)

@JsonClass(generateAdapter = true)
data class ActiveBet(
    @Json(name = "id") val id: String,
    @Json(name = "goal_node_id") val goalNodeId: String?,
    @Json(name = "stake_points") val stakePoints: Int?,
    @Json(name = "deadline") val deadline: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "goal_name") val goalName: String?,
)

@JsonClass(generateAdapter = true)
data class DailyBrief(
    @Json(name = "date") val date: String,
    @Json(name = "brief") val brief: String?,
    @Json(name = "generated_at") val generatedAt: String?,
)
