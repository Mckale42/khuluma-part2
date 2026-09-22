package com.viltrumites.khuluma.ui.lesson

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viltrumites.khuluma.data.remote.ExerciseDto
import com.viltrumites.khuluma.ui.components.AnimatedCounter
import com.viltrumites.khuluma.ui.components.AnimatedProgressBar
import com.viltrumites.khuluma.ui.components.BrandButton
import com.viltrumites.khuluma.ui.components.ConfettiOverlay
import com.viltrumites.khuluma.ui.components.Mascot
import com.viltrumites.khuluma.ui.theme.Brand

@Composable
fun LessonScreen(lessonId: String, onDone: () -> Unit, vm: LessonViewModel = viewModel()) {
    LaunchedEffect(lessonId) { vm.load(lessonId) }
    val state by vm.state.collectAsState()

    when (val s = state) {
        is LessonState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Mascot(size = 64.dp); Text("Loading…", color = Brand.Muted) }
        }
        is LessonState.Error -> Box(Modifier.fillMaxSize().padding(24.dp), Alignment.Center) {
            Text(s.message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }
        is LessonState.Running -> {
            val ui = s.ui
            if (ui.phase == Phase.COMPLETE) { CompleteView(ui, onDone); return }
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                AnimatedProgressBar(if (ui.phase == Phase.FEEDBACK) (ui.index + 1f) / ui.exercises.size else ui.progress, Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                when (ui.phase) {
                    Phase.QUESTION -> QuestionView(ui.current, ui.submitting) { vm.submit(it) }
                    Phase.FEEDBACK -> FeedbackView(ui, onExplain = { vm.explain() }, onContinue = { vm.next() })
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun QuestionView(ex: ExerciseDto, submitting: Boolean, onSubmit: (String) -> Unit) {
    var selected by remember(ex.id) { mutableStateOf("") }
    var typed by remember(ex.id) { mutableStateOf("") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        if (ex.type == "listen") {
            Box(
                Modifier.clip(RoundedCornerShape(14.dp)).background(Brand.buttonGradient).clickable { }.padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.VolumeUp, contentDescription = "Play audio", tint = Color.White)
                    Spacer(Modifier.width(8.dp)); Text("Play", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(14.dp))
        }
        Text(ex.prompt, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Brand.Ink)
        Spacer(Modifier.height(20.dp))

        if (ex.type == "freetext") {
            OutlinedTextField(
                value = typed, onValueChange = { typed = it },
                label = { Text("Type your answer") }, singleLine = true,
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            BrandButton(if (submitting) "Checking…" else "Check", { onSubmit(typed.trim()) }, Modifier.fillMaxWidth(), enabled = typed.isNotBlank() && !submitting)
        } else {
            ex.options.forEach { option -> OptionCard(option, option == selected) { selected = option } }
            Spacer(Modifier.height(16.dp))
            BrandButton(if (submitting) "Checking…" else "Check", { onSubmit(selected) }, Modifier.fillMaxWidth(), enabled = selected.isNotBlank() && !submitting)
        }
    }
}

@Composable
private fun OptionCard(text: String, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) Brand.SelectBg else Brand.CardWhite, label = "bg")
    val border by animateColorAsState(if (selected) Brand.Purple else Brand.TrackBg, label = "border")
    val scale by animateFloatAsState(if (selected) 1.03f else 1f, label = "scale")
    Box(
        Modifier.fillMaxWidth().padding(vertical = 6.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(2.dp, border, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) { Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Brand.Ink) }
}

@Composable
private fun FeedbackView(ui: LessonUi, onExplain: () -> Unit, onContinue: () -> Unit) {
    val result = ui.lastResult ?: return
    val correct = result.correct
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Mascot(size = 48.dp, bob = correct)
            Spacer(Modifier.width(8.dp))
            Text(
                if (correct) "Correct! +${result.xpAwarded} XP" else "Not quite",
                fontWeight = FontWeight.Bold, fontSize = 22.sp,
                color = if (correct) Brand.Teal else Brand.Flame
            )
        }
        Spacer(Modifier.height(8.dp))
        if (!correct) {
            Text("Answer: ${result.correctAnswer}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Brand.Ink)
            Spacer(Modifier.height(12.dp))
            BrandButton(if (ui.explaining) "Thinking…" else "Why? — explain (AI)", onExplain, Modifier.fillMaxWidth(), enabled = !ui.explaining)
            AnimatedVisibility(ui.explanation != null) {
                Card(Modifier.fillMaxWidth().padding(top = 12.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Brand.CardWhite)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Buddy", color = Brand.Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(ui.explanation ?: "", color = Brand.Ink)
                    }
                }
            }
        }
        if (result.newAchievements.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text("Achievement unlocked: ${result.newAchievements.joinToString()}", color = Brand.Gold, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(24.dp))
        BrandButton("Continue", onContinue, Modifier.fillMaxWidth())
    }
}

@Composable
private fun CompleteView(ui: LessonUi, onDone: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        ConfettiOverlay(Modifier.fillMaxSize())
        Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            Mascot(size = 96.dp)
            Spacer(Modifier.height(12.dp))
            Text("Lesson complete!", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Brand.PurpleDeep)
            Spacer(Modifier.height(12.dp))
            AnimatedCounter(ui.totalGainedXp, MaterialTheme.typography.headlineLarge.copy(fontSize = 46.sp, fontWeight = FontWeight.Bold), Brand.PurpleDeep)
            Text("XP earned", color = Brand.Muted)
            Spacer(Modifier.height(8.dp))
            Text("${ui.correctCount}/${ui.exercises.size} correct", fontWeight = FontWeight.Bold, color = Brand.Ink)
            Spacer(Modifier.height(32.dp))
            BrandButton("Back to path", onDone, Modifier.fillMaxWidth(0.75f))
        }
    }
}
