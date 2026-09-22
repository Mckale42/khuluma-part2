package com.viltrumites.khuluma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viltrumites.khuluma.ui.theme.Brand

/**
 * Gamification status bar: streak (pulsing flame), animated XP and level, as
 * rounded pills. `light = true` styles it for the dark gradient header.
 */
@Composable
fun StatsBar(streak: Int, xp: Int, level: Int, light: Boolean = false, modifier: Modifier = Modifier) {
    val fg = if (light) Color.White else Brand.PurpleDeep
    val chipBg = if (light) Color.White.copy(alpha = 0.16f) else Brand.TrackBg
    Row(modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Pill(chipBg) { PulsingFlame(tint = if (light) Brand.GoldSoft else Brand.Flame); Spacer(Modifier.width(4.dp)); Text("$streak", color = fg, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
        Pill(chipBg) { IconTiny(Icons.Filled.Bolt, if (light) Brand.GoldSoft else Brand.Gold); Spacer(Modifier.width(4.dp)); AnimatedCounter(xp, MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold), fg) }
        Pill(chipBg) { IconTiny(Icons.Filled.MilitaryTech, if (light) Brand.GoldSoft else Brand.Purple); Spacer(Modifier.width(4.dp)); Text("Lv $level", color = fg, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
    }
}

@Composable
private fun Pill(bg: Color, content: @Composable () -> Unit) {
    Row(
        Modifier.background(bg, RoundedCornerShape(14.dp)).padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { content() }
}

@Composable
private fun IconTiny(icon: ImageVector, tint: Color) = Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.width(18.dp))
