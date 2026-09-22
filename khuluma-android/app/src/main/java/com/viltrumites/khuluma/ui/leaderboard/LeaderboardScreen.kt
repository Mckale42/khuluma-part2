package com.viltrumites.khuluma.ui.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.util.UiState

@Composable
fun LeaderboardScreen(vm: LeaderboardViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    when (val s = state) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        is UiState.Error -> Box(Modifier.fillMaxSize().padding(24.dp), Alignment.Center) { Text(s.message, color = MaterialTheme.colorScheme.error) }
        is UiState.Success -> {
            val lb = s.data
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text("Weekly League", style = MaterialTheme.typography.headlineMedium)
                Text(lb.weekKey, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                if (lb.ranking.isEmpty()) {
                    Text("No XP earned this week yet — be the first!", style = MaterialTheme.typography.bodyLarge)
                } else {
                    LazyColumn {
                        itemsIndexed(lb.ranking) { _, entry ->
                            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text("#${entry.rank}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.width(16.dp))
                                    Text(entry.displayName, Modifier.weight(1f))
                                    Text("${entry.xp} XP", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
        else -> {}
    }
}
