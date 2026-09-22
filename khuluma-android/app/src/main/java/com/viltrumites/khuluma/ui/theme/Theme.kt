package com.viltrumites.khuluma.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KhulumaColors = lightColorScheme(
    primary = Brand.Purple,
    onPrimary = Brand.CardWhite,
    primaryContainer = Color_EDEAFF,
    onPrimaryContainer = Brand.PurpleDeep,
    secondary = Brand.Teal,
    tertiary = Brand.Gold,
    error = Brand.Flame,
    background = Brand.Lavender,
    surface = Brand.CardWhite,
    onBackground = Brand.Ink,
    onSurface = Brand.Ink,
    surfaceVariant = Color_EDEAFF,
    onSurfaceVariant = Brand.PurpleDeep
)

// Keep the app in its designed light identity for a consistent, branded look.
@Composable
fun KhulumaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = KhulumaColors, typography = KhulumaTypography, shapes = KhulumaShapes, content = content)
}
