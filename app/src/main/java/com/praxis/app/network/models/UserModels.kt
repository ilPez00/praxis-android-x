package com.praxis.app.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfileResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "city") val city: String?,
    @Json(name = "is_premium") val isPremium: Boolean?,
    @Json(name = "is_admin") val isAdmin: Boolean?,
    @Json(name = "praxis_points") val praxisPoints: Int?,
    @Json(name = "current_streak") val currentStreak: Int?,
    @Json(name = "honor_score") val honorScore: Double?,
    @Json(name = "reliability_score") val reliabilityScore: Double?,
    @Json(name = "onboarding_completed") val onboardingCompleted: Boolean?,
    @Json(name = "created_at") val createdAt: String?,
)

@JsonClass(generateAdapter = true)
data class ProfileUpdateRequest(
    @Json(name = "name") val name: String? = null,
    @Json(name = "bio") val bio: String? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "city") val city: String? = null,
    @Json(name = "occupation") val occupation: String? = null,
    @Json(name = "education") val education: String? = null,
    @Json(name = "sex") val sex: String? = null,
    @Json(name = "language") val language: String? = null,
)

@JsonClass(generateAdapter = true)
data class ProfileUpdateResponse(
    @Json(name = "message") val message: String,
    @Json(name = "user") val user: UserProfileResponse,
)

@JsonClass(generateAdapter = true)
data class MyProfileWrapper(
    @Json(name = "user") val user: UserProfileResponse,
)
