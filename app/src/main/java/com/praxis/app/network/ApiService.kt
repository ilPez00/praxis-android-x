package com.praxis.app.network

import com.praxis.app.network.models.*
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body body: Map<String, String>): Map<String, Any>

    @POST("api/auth/signup")
    suspend fun signup(@Body body: Map<String, Any>): Map<String, Any>

    // ── Goals ────────────────────────────────────────────────────────────

    @GET("api/goals/{userId}")
    suspend fun getGoalTree(@Path("userId") userId: String): GoalTreeResponse

    @PATCH("api/goals/{userId}/node/{nodeId}/progress")
    suspend fun updateNodeProgress(
        @Path("userId") userId: String,
        @Path("nodeId") nodeId: String,
        @Body body: ProgressUpdateRequest,
    ): ProgressUpdateResponse

    @POST("api/goals/{userId}/node")
    suspend fun createGoalNode(
        @Path("userId") userId: String,
        @Body body: CreateGoalNodeRequest,
    ): CreateGoalNodeResponse

    @PATCH("api/goals/{userId}/node/{nodeId}")
    suspend fun updateGoalNode(
        @Path("userId") userId: String,
        @Path("nodeId") nodeId: String,
        @Body body: UpdateGoalNodeRequest,
    ): UpdateGoalNodeResponse

    @DELETE("api/goals/{userId}/node/{nodeId}")
    suspend fun deleteGoalNode(
        @Path("userId") userId: String,
        @Path("nodeId") nodeId: String,
    ): DeleteGoalNodeResponse

    // ── Dashboard ────────────────────────────────────────────────────────

    @GET("api/dashboard/summary")
    suspend fun getDashboardSummary(@Query("userId") userId: String): DashboardSummaryResponse

    // ── Checkins ─────────────────────────────────────────────────────────

    @POST("api/checkins")
    suspend fun postCheckin(@Body body: CheckinRequest): CheckinResponse

    @GET("api/checkins/today")
    suspend fun getTodayCheckin(): CheckinResponse?

    // ── Notebook ─────────────────────────────────────────────────────────

    @POST("api/notebook/entries")
    suspend fun createNotebookEntry(@Body body: NotebookEntryRequest): NotebookEntryResponse

    @GET("api/notebook/entries")
    suspend fun getNotebookEntries(
        @Query("entry_type") entryType: String? = null,
        @Query("domain") domain: String? = null,
        @Query("tag") tag: String? = null,
        @Query("search") search: String? = null,
        @Query("goal_id") goalId: String? = null,
        @Query("since") since: String? = null,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
    ): List<NotebookEntryResponse>

    @GET("api/notebook/stats")
    suspend fun getNotebookStats(): Map<String, Any>

    @PATCH("api/notebook/entries/{id}")
    suspend fun updateNotebookEntry(
        @Path("id") id: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>,
    ): NotebookEntryResponse

    @DELETE("api/notebook/entries/{id}")
    suspend fun deleteNotebookEntry(@Path("id") id: String): Map<String, String>

    @GET("api/notebook/tags")
    suspend fun getNotebookTags(): List<Map<String, Any>>

    @POST("api/notebook/tags")
    suspend fun createNotebookTag(@Body body: Map<String, String>): Map<String, Any>

    @DELETE("api/notebook/tags/{id}")
    suspend fun deleteNotebookTag(@Path("id") id: String): Map<String, String>

    // ── Users ────────────────────────────────────────────────────────────

    @GET("api/users/me")
    suspend fun getMyProfile(): MyProfileWrapper

    @GET("api/users/{id}")
    suspend fun getUserProfile(@Path("id") id: String): UserProfileResponse

    @PUT("api/users/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Body body: ProfileUpdateRequest,
    ): ProfileUpdateResponse

    @POST("api/users/complete-onboarding")
    suspend fun completeOnboarding(): Map<String, Any>

    // ── Trackers ─────────────────────────────────────────────────────────

    @POST("api/trackers/log")
    suspend fun logTracker(@Body body: TrackerLogRequest): Map<String, Any>

    @GET("api/trackers/my")
    suspend fun getMyTrackers(): List<TrackerDef>

    @GET("api/trackers/calendar")
    suspend fun getTrackerCalendar(): Map<String, Any>
}
