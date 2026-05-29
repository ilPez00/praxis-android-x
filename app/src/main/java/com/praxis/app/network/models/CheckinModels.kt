package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckinRequest(
    @Json(name = "mood") val mood: String,
    @Json(name = "win_of_the_day") val winOfTheDay: String? = null,
)

@JsonClass(generateAdapter = true)
data class CheckinResponse(
    @Json(name = "id") val id: String,
    @Json(name = "mood") val mood: String,
    @Json(name = "streak_day") val streakDay: Int,
    @Json(name = "praxis_points_earned") val pointsEarned: Int,
)
