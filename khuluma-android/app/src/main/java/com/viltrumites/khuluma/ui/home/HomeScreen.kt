package com.viltrumites.khuluma.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.data.remote.LessonSummary
import com.viltrumites.khuluma.data.remote.WordOfDay
import com.viltrumites.khuluma.ui.components.AnimatedProgressBar
import com.viltrumites.khuluma.ui.components.Mascot
import com.viltrumites.khuluma.ui.components.StatsBar
import com.viltrumites.khuluma.ui.theme.Brand
import com.viltrumites.khuluma.util.UiState

private fun levelFloor(level: Int) = 100 * ((level - 1) * level) / 2
private fun levelCeil(level: Int) = 100 * (level * (level + 1)) / 2

@Composable
fun HomeScreen(onOpenLesson: (String) -> Unit, vm: HomeViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    when (val s = state) {
        is UiState.Loading -> LoadingBird()
        is UiState.Error -> Column(
            Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Text(s.message, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { vm.load() }) { Text("Retry") }
        }
        is UiState.Success -> {
            val data = s.data
            LazyColumn(
                Modifier.fillMaxSize().background(Brand.Lavender),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item { Header(data.me.displayName, data.me.streakCount, data.me.totalXp, data.me.level) }
                item { WordOfDayCard(data.word) }
                item {
                    Text(
                        "Continue learning",
                        color = Brand.Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 2.dp)
                    )
                }
                data.path.units.forEach { unit ->
                    item {
                        Text(
                            unit.title.uppercase(),
                            color = Brand.Muted, fontWeight = FontWeight.Bold, fontSize = 12.sp,
                            modifier = Modifier.padding(start = 20.dp, top = 14.dp, bottom = 6.dp)
                        )
                    }
                    itemsIndexed(unit.lessons) { idx, lesson -> LessonRow(idx + 1, lesson, onOpenLesson) }
                }
            }
        }
        else -> {}
    }
}

@Composable
private fun Header(name: String, streak: Int, xp: Int, level: Int) {
    val floor = levelFloor(level)
    val ceil = levelCeil(level)
    val levelProgress = ((xp - floor).toFloat() / (ceil - floor).coerceAtLeast(1)).coerceIn(0f, 1f)
    val toNext = (ceil - xp).coerceAtLeast(0)

    Column(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brand.headerGradient)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Mascot(size = 54.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Sawubona,", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        StatsBar(streak = streak, xp = xp, level = level, light = true)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Level $level", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("$toNext XP to level ${level + 1}", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
        }
        Spacer(Modifier.height(6.dp))
        AnimatedProgressBar(levelProgress, Modifier.fillMaxWidth())
    }
}

@Composable
private fun WordOfDayCard(word: WordOfDay) {
    if (word.word == null) return
    Card(
        Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Brand.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Brand.SelectBg),
                contentAlignment = Alignment.Center
            ) { Text("zu", color = Brand.PurpleDeep, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("WORD OF THE DAY", color = Brand.Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Spacer(Modifier.height(2.dp))
                Text("${word.word} — ${word.translation}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Brand.Ink)
                word.note?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(it, color = Brand.Muted, fontSize = 12.sp, maxLines = 2)
                }
            }
        }
    }
}

@Composable
private fun LessonRow(number: Int, lesson: LessonSummary, onOpen: (String) -> Unit) {
    Card(
        onClick = { onOpen(lesson.lessonId) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Brand.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(48.dp).clip(CircleShape).background(Brand.headerGradient),
                contentAlignment = Alignment.Center
            ) { Text("$number", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(lesson.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Brand.Ink)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = Brand.Gold, modifier = Modifier.size(15.dp))
                    Text(" +${lesson.xpReward} XP  ·  ${lesson.exercises} exercises", color = Brand.Muted, fontSize = 13.sp)
                }
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Brand.Lock, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun LoadingBird() {
    Column(Modifier.fillMaxSize().background(Brand.Lavender), Arrangement.Center, Alignment.CenterHorizontally) {
        Mascot(size = 72.dp)
        Spacer(Modifier.height(12.dp))
        Text("Loading your path…", color = Brand.Muted)
    }
}
