package com.aura.moodtracker.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF007BFF)
val BackgroundLight = Color(0xFFF8F9FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val AccentGreen = Color(0xFF28A745)
val NegativeRed = Color(0xFFDC3545)
val NeutralGrey = Color(0xFF6C757D)
val ShakeGrowth = Color(0xFFFFC107)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    background = BackgroundLight,
    surface = SurfaceWhite,
    secondary = AccentGreen,
    error = NegativeRed,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun AuraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}