package com.viltrumites.khuluma.data.remote

/** Data-transfer objects mirroring the Khuluma REST API JSON (design doc §7). */

data class Settings(
    val dailyGoalXp: Int = 50,
    val uiLanguage: String = "en",
    val notifications: Boolean = true,
    val sound: Boolean = true,
    val darkMode: Boolean = false,
    val biometricEnabled: Boolean = false
)

data class MeResponse(
    val userId: String,
    val displayName: String,
    val email: String,
    val learningLanguage: String,
    val totalXp: Int,
    val streakCount: Int,
    val streakFreezes: Int,
    val level: Int,
    val achievements: List<String> = emptyList(),
    val settings: Settings = Settings()
)

data class SettingsResponse(val ok: Boolean, val settings: Settings)

data class LessonSummary(val lessonId: String, val title: String, val xpReward: Int, val exercises: Int)
data class UnitDto(val unitId: String, val title: String, val lessons: List<LessonSummary>)
data class CoursePath(val lang: String, val title: String, val units: List<UnitDto>)

data class ExerciseDto(
    val id: String,
    val type: String,               // mcq | listen | match | freetext
    val prompt: String,
    val options: List<String> = emptyList(),
    val audioUrl: String = ""
)
data class NextExercises(val lessonId: String, val title: String, val xpReward: Int, val exercises: List<ExerciseDto>)

data class AttemptBody(val exerciseId: String, val lessonId: String, val answer: String, val responseMs: Long)
data class AttemptResult(
    val correct: Boolean,
    val correctAnswer: String = "",
    val xpAwarded: Int,
    val totalXp: Int,
    val level: Int,
    val streakCount: Int,
    val streakFreezes: Int,
    val newAchievements: List<String> = emptyList()
)

data class ChatBody(val message: String)
data class ChatResponse(val reply: String)
data class ExplainBody(val prompt: String, val userAnswer: String, val correctAnswer: String)
data class ExplainResponse(val explanation: String)

data class WordOfDay(val word: String?, val translation: String?, val note: String?, val audioUrl: String?)

data class RankEntry(val rank: Int, val displayName: String, val xp: Int)
data class Leaderboard(val weekKey: String, val ranking: List<RankEntry>)
