package com.viltrumites.khuluma.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Khuluma brand identity — a distinctive, gamified, South-African-inspired look
 * (deep purple base, warm gold/coral accents, teal for success). Centralised so
 * every screen shares one visual language rather than default Material styling.
 */
object Brand {
    val Purple = Color(0xFF6B4CD6)
    val PurpleDeep = Color(0xFF4A2FA8)
    val PurpleInk = Color(0xFF3A2496)
    val Gold = Color(0xFFFFB020)
    val GoldSoft = Color(0xFFFFD37A)
    val Flame = Color(0xFFFF6B4A)
    val Teal = Color(0xFF1FA9A0)
    val TealSoft = Color(0xFF57D3B6)
    val Sky = Color(0xFF378ADD)
    val Lavender = Color(0xFFF3F1FB)
    val CardWhite = Color(0xFFFFFFFF)
    val Ink = Color(0xFF211D38)
    val Muted = Color(0xFF6B678A)
    val Lock = Color(0xFFC9C4E6)
    val TrackBg = Color(0xFFE4E0F3)
    val SelectBg = Color(0xFFEDEAFF)

    val headerGradient = Brush.linearGradient(listOf(Purple, PurpleDeep, PurpleInk))
    val buttonGradient = Brush.linearGradient(listOf(Purple, PurpleDeep))
    val xpGradient = Brush.horizontalGradient(listOf(Teal, TealSoft))

    // Beadwork accent stripe colours (Ndebele/Zulu-inspired), used as a divider.
    val bead = listOf(Gold, Flame, Teal, Lavender)
}
