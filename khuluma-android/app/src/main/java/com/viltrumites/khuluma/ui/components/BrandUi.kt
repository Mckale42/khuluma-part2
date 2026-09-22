package com.viltrumites.khuluma.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viltrumites.khuluma.ui.theme.Brand
import kotlin.math.roundToInt
import kotlin.random.Random

/** A number that animates up from zero (XP, totals). */
@Composable
fun AnimatedCounter(target: Int, style: TextStyle = MaterialTheme.typography.headlineMedium, color: Color = Brand.PurpleDeep) {
    val anim = remember { Animatable(0f) }
    androidx.compose.runtime.LaunchedEffect(target) { anim.animateTo(target.toFloat(), tween(1100)) }
    Text(anim.value.roundToInt().toString(), color = color, style = style)
}

/** Progress bar that smoothly fills to the target fraction. */
@Composable
fun AnimatedProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val p by animateFloatAsState(progress.coerceIn(0f, 1f), tween(800, easing = FastOutSlowInEasing), label = "progress")
    Box(modifier.height(10.dp).clip(RoundedCornerShape(6.dp)).background(Brand.TrackBg)) {
        Box(Modifier.fillMaxWidth(p).fillMaxHeight().clip(RoundedCornerShape(6.dp)).background(Brand.xpGradient))
    }
}

/** Streak flame that gently pulses. */
@Composable
fun PulsingFlame(tint: Color = Brand.GoldSoft, size: Dp = 18.dp) {
    val t = rememberInfiniteTransition(label = "flame")
    val s by t.animateFloat(1f, 1.22f, infiniteRepeatable(tween(1100), RepeatMode.Reverse), label = "s")
    Icon(Icons.Filled.LocalFireDepartment, contentDescription = "streak", tint = tint,
        modifier = Modifier.size(size).graphicsLayer { scaleX = s; scaleY = s })
}

/** Three blinking dots for the AI "typing" state. */
@Composable
fun TypingDots() {
    val t = rememberInfiniteTransition(label = "dots")
    Row {
        for (i in 0..2) {
            val a by t.animateFloat(0.3f, 1f, infiniteRepeatable(tween(600, delayMillis = i * 180), RepeatMode.Reverse), label = "d$i")
            Box(Modifier.padding(2.dp).size(7.dp).clip(CircleShape).background(Brand.Muted.copy(alpha = a)))
        }
    }
}

/** Chunky gradient button that scales down when pressed. */
@Composable
fun BrandButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "press")
    Box(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) Brand.buttonGradient else SolidColor(Brand.Lock))
            .clickable(interactionSource = interaction, indication = null, enabled = enabled) { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) { Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
}

/** Ndebele/Zulu-inspired beadwork accent stripe. */
@Composable
fun BeadStripe(modifier: Modifier = Modifier.fillMaxWidth().height(8.dp)) {
    Canvas(modifier) {
        val cols = Brand.bead
        val bw = 24f
        var x = 0f; var i = 0
        while (x < size.width) {
            drawRect(cols[i % cols.size], topLeft = Offset(x, 0f), size = Size(bw, size.height))
            x += bw; i++
        }
    }
}

private data class Piece(val xFrac: Float, val color: Color, val phase: Float, val w: Float, val h: Float, val speed: Float)

/** Falling, spinning confetti — celebratory overlay for lesson completion. */
@Composable
fun ConfettiOverlay(modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight(), count: Int = 26) {
    val cols = listOf(Brand.Gold, Brand.Flame, Brand.Teal, Brand.Purple, Brand.Sky)
    val pieces = remember {
        List(count) { Piece(Random.nextFloat(), cols[it % cols.size], Random.nextFloat(), 8f + Random.nextFloat() * 6f, 12f + Random.nextFloat() * 8f, 0.7f + Random.nextFloat() * 0.6f) }
    }
    val trans = rememberInfiniteTransition(label = "confetti")
    val t by trans.animateFloat(0f, 1f, infiniteRepeatable(tween(2600, easing = LinearEasing)), label = "t")
    Canvas(modifier) {
        pieces.forEach { pc ->
            val prog = ((t * pc.speed) + pc.phase) % 1f
            val x = pc.xFrac * size.width
            val y = prog * size.height
            rotate(prog * 540f, pivot = Offset(x, y)) {
                drawRect(pc.color, topLeft = Offset(x, y), size = Size(pc.w, pc.h))
            }
        }
    }
}
