package com.viltrumites.khuluma.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onSignedOut: () -> Unit, vm: SettingsViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    when (val s = state) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        is UiState.Error -> Box(Modifier.fillMaxSize().padding(24.dp), Alignment.Center) { Text(s.message, color = MaterialTheme.colorScheme.error) }
        is UiState.Success -> {
            val settings = s.data
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text("Settings", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))

                SwitchRow("Notifications", settings.notifications) { vm.update(settings.copy(notifications = it)) }
                SwitchRow("Sound effects", settings.sound) { vm.update(settings.copy(sound = it)) }
                SwitchRow("Dark mode", settings.darkMode) { vm.update(settings.copy(darkMode = it)) }

                Spacer(Modifier.height(16.dp))
                Text("Daily goal: ${settings.dailyGoalXp} XP", style = MaterialTheme.typography.titleMedium)
                Row {
                    listOf(20, 50, 100).forEach { goal ->
                        FilterChip(
                            selected = settings.dailyGoalXp == goal,
                            onClick = { vm.update(settings.copy(dailyGoalXp = goal)) },
                            label = { Text("$goal") },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { scope.launch { vm.signOut(); onSignedOut() } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Sign out") }
            }
        }
        else -> {}
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
