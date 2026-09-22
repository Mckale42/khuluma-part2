package com.viltrumites.khuluma.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.viltrumites.khuluma.ui.theme.Brand

/**
 * "Zola" — Khuluma's weaver-bird mascot, drawn entirely with Compose Canvas
 * (our own character, no external assets). Optionally bobs up and down.
 */
@Composable
fun Mascot(size: Dp = 56.dp, bob: Boolean = true, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "mascot")
    val dy by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (bob) -7f else 0f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "bob"
    )
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        translate(top = dy) {
            // body
            drawOval(Brand.Gold, topLeft = Offset(w * 0.18f, w * 0.24f), size = Size(w * 0.64f, w * 0.66f))
            // wing
            val wing = Path().apply {
                moveTo(w * 0.34f, w * 0.5f)
                quadraticBezierTo(w * 0.12f, w * 0.58f, w * 0.2f, w * 0.82f)
                quadraticBezierTo(w * 0.34f, w * 0.7f, w * 0.44f, w * 0.66f)
                close()
            }
            drawPath(wing, Brand.PurpleDeep)
            // eye patch + pupil
            drawCircle(Color.White, radius = w * 0.15f, center = Offset(w * 0.63f, w * 0.46f))
            drawCircle(Brand.Ink, radius = w * 0.07f, center = Offset(w * 0.66f, w * 0.48f))
            drawCircle(Color.White, radius = w * 0.022f, center = Offset(w * 0.685f, w * 0.455f))
            // beak
            val beak = Path().apply {
                moveTo(w * 0.78f, w * 0.5f); lineTo(w * 0.94f, w * 0.46f); lineTo(w * 0.78f, w * 0.58f); close()
            }
            drawPath(beak, Brand.Flame)
            // feet
            val foot = Path().apply {
                moveTo(w * 0.42f, w * 0.9f); lineTo(w * 0.47f, w * 0.78f); lineTo(w * 0.52f, w * 0.9f); close()
            }
            drawPath(foot, Color(0xFFE39A1F))
        }
    }
}
