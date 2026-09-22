package com.viltrumites.khuluma.ui.navigation

/** Central list of navigation routes. */
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val LESSON = "lesson/{lessonId}"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val BUDDY = "buddy"
    const val LEADERBOARD = "leaderboard"
    fun lesson(id: String) = "lesson/$id"
}
