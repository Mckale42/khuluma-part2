package com.viltrumites.khuluma.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.viltrumites.khuluma.ui.auth.LoginScreen
import com.viltrumites.khuluma.ui.buddy.BuddyScreen
import com.viltrumites.khuluma.ui.components.BottomBar
import com.viltrumites.khuluma.ui.home.HomeScreen
import com.viltrumites.khuluma.ui.leaderboard.LeaderboardScreen
import com.viltrumites.khuluma.ui.lesson.LessonScreen
import com.viltrumites.khuluma.ui.profile.ProfileScreen
import com.viltrumites.khuluma.ui.settings.SettingsScreen

private val tabRoutes = setOf(Routes.HOME, Routes.BUDDY, Routes.LEADERBOARD, Routes.PROFILE)

@Composable
fun KhulumaApp() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (current in tabRoutes) {
                BottomBar(current) { route ->
                    nav.navigate(route) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            nav,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 6 } },
            exitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 8 } },
            popEnterTransition = { fadeIn(tween(250)) + slideInHorizontally(tween(250)) { -it / 6 } },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 8 } }
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(onSignedIn = {
                    nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                })
            }
            composable(Routes.HOME) { HomeScreen(onOpenLesson = { nav.navigate(Routes.lesson(it)) }) }
            composable(Routes.LESSON) { entry ->
                val lessonId = entry.arguments?.getString("lessonId") ?: ""
                LessonScreen(lessonId = lessonId, onDone = { nav.popBackStack() })
            }
            composable(Routes.BUDDY) { BuddyScreen() }
            composable(Routes.LEADERBOARD) { LeaderboardScreen() }
            composable(Routes.PROFILE) { ProfileScreen(onSettings = { nav.navigate(Routes.SETTINGS) }) }
            composable(Routes.SETTINGS) {
                SettingsScreen(onSignedOut = {
                    nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                })
            }
        }
    }
}
