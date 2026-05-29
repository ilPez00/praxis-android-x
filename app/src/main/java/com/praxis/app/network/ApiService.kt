package com.praxis.app.network

import com.praxis.app.network.models.*
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body body: Map<String, String>): Map<String, Any>

    @POST("api/auth/signup")
    suspend fun signup(@Body body: Map<String, Any>): Map<String, Any>

    @GET("api/goals/{userId}")
    suspend fun getGoalTree(@Path("userId") userId: String): GoalTreeResponse

    @POST("api/checkins")
    suspend fun postCheckin(@Body body: CheckinRequest): CheckinResponse

    @GET("api/checkins/today")
    suspend fun getTodayCheckin(): CheckinResponse?

    @POST("api/notebook/entries")
    suspend fun createNotebookEntry(@Body body: NotebookEntryRequest): NotebookEntryResponse

    @GET("api/notebook/entries")
    suspend fun getNotebookEntries(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
    ): List<NotebookEntryResponse>

    @POST("api/trackers/log")
    suspend fun logTracker(@Body body: TrackerLogRequest): Map<String, Any>

    @GET("api/trackers/my")
    suspend fun getMyTrackers(): List<TrackerDef>

    @GET("api/notebook/stats")
    suspend fun getNotebookStats(): Map<String, Any>

    @GET("api/trackers/calendar")
    suspend fun getTrackerCalendar(): Map<String, Any>
}
