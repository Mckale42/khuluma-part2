package com.viltrumites.khuluma.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.util.UiState
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onSignedOut: () -> Unit,
    vm: SettingsViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    when (val s = state) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = s.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is UiState.Success -> {
            val settings = s.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                SwitchRow(
                    label = "Notifications",
                    checked = settings.notifications
                ) {
                    vm.update(settings.copy(notifications = it))
                }

                SwitchRow(
                    label = "Sound effects",
                    checked = settings.sound
                ) {
                    vm.update(settings.copy(sound = it))
                }

                SwitchRow(
                    label = "Dark mode",
                    checked = settings.darkMode
                ) {
                    vm.update(settings.copy(darkMode = it))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Daily goal: ${settings.dailyGoalXp} XP",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    listOf(20, 50, 100).forEach { goal ->
                        FilterChip(
                            selected = settings.dailyGoalXp == goal,
                            onClick = {
                                vm.update(
                                    settings.copy(
                                        dailyGoalXp = goal
                                    )
                                )
                            },
                            label = {
                                Text("$goal")
                            },
                            modifier = Modifier.semantics {
                                contentDescription = "$goal XP daily goal"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        scope.launch {
                            vm.signOut()
                            onSignedOut()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Sign out of Khuluma"
                        }
                ) {
                    Text("Sign out")
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = checked,
            onCheckedChange = onChange,
            modifier = Modifier.semantics {
                contentDescription = "$label setting"
            }
        )
    }
}