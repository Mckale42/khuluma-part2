package com.viltrumites.khuluma.data.repo

import com.viltrumites.khuluma.data.remote.*

/**
 * Repository — the single gateway the UI uses to reach the API. Keeps ViewModels
 * free of Retrofit details (design doc: MVVM, UI separated from data).
 */
class KhulumaRepository(private val api: KhulumaApi) {
    suspend fun me(): MeResponse = api.getMe()
    suspend fun updateSettings(settings: Settings): Settings = api.updateSettings(settings).settings
    suspend fun path(lang: String = "zu"): CoursePath = api.getPath(lang)
    suspend fun nextExercises(lessonId: String): NextExercises = api.getNext(lessonId)
    suspend fun submit(body: AttemptBody): AttemptResult = api.submitAttempt(body)
    suspend fun chat(message: String): String = api.chat(ChatBody(message)).reply
    suspend fun explain(prompt: String, userAnswer: String, correct: String): String =
        api.explain(ExplainBody(prompt, userAnswer, correct)).explanation
    suspend fun wordOfDay(): WordOfDay = api.wordOfDay()
    suspend fun leaderboard(): Leaderboard = api.leaderboard()
}
