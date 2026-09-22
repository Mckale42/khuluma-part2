package com.viltrumites.khuluma.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.viltrumites.khuluma.ui.navigation.Routes

data class NavTab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val tabs = listOf(
    NavTab(Routes.HOME, "Learn", Icons.Filled.Home),
    NavTab(Routes.BUDDY, "Buddy", Icons.Filled.SmartToy),
    NavTab(Routes.LEADERBOARD, "League", Icons.Filled.Leaderboard),
    NavTab(Routes.PROFILE, "Profile", Icons.Filled.Person)
)

/** One-tap bottom navigation (design doc §4: Learn, Buddy, League, Profile). */
@Composable
fun BottomBar(current: String?, onNavigate: (String) -> Unit) {
    NavigationBar {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = current == tab.route,
                onClick = { if (current != tab.route) onNavigate(tab.route) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}
