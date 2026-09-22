package com.viltrumites.khuluma.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.ui.components.StatsBar
import com.viltrumites.khuluma.util.UiState

@Composable
fun ProfileScreen(onSettings: () -> Unit, vm: ProfileViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    when (val s = state) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        is UiState.Error -> Box(Modifier.fillMaxSize().padding(24.dp), Alignment.Center) { Text(s.message, color = MaterialTheme.colorScheme.error) }
        is UiState.Success -> {
            val me = s.data
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                Text(me.displayName, style = MaterialTheme.typography.headlineMedium)
                Text(me.email, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                StatsBar(streak = me.streakCount, xp = me.totalXp, level = me.level)
                Spacer(Modifier.height(16.dp))
                Text("Achievements", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                if (me.achievements.isEmpty()) {
                    Text("No badges yet — complete a lesson to earn your first!", style = MaterialTheme.typography.bodyMedium)
                } else {
                    me.achievements.forEach { code ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(code, Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
            }
        }
        else -> {}
    }
}
