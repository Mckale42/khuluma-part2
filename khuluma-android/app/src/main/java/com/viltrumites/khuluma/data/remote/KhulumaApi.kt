package com.viltrumites.khuluma.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Type-safe HTTP contract for the Khuluma REST API (design doc §5.2). */
interface KhulumaApi {
    @GET("me")
    suspend fun getMe(): MeResponse

    @PUT("me/settings")
    suspend fun updateSettings(@Body settings: Settings): SettingsResponse

    @GET("courses/{lang}/path")
    suspend fun getPath(@Path("lang") lang: String): CoursePath

    @GET("lessons/{id}/next")
    suspend fun getNext(@Path("id") lessonId: String): NextExercises

    @POST("attempts")
    suspend fun submitAttempt(@Body body: AttemptBody): AttemptResult

    @POST("ai/chat")
    suspend fun chat(@Body body: ChatBody): ChatResponse

    @POST("ai/explain")
    suspend fun explain(@Body body: ExplainBody): ExplainResponse

    @GET("word-of-day")
    suspend fun wordOfDay(@Query("lang") lang: String = "zu"): WordOfDay

    @GET("leaderboard")
    suspend fun leaderboard(): Leaderboard
}
