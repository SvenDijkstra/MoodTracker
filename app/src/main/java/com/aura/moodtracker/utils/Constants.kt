package com.aura.moodtracker.utils

object Constants {
    // Mood Scale Constants
    const val MOOD_VERY_SAD = 0
    const val MOOD_SAD = 1
    const val MOOD_NEUTRAL = 2
    const val MOOD_HAPPY = 3
    const val MOOD_VERY_HAPPY = 4
    
    // Mood Labels
    val MOOD_LABELS = mapOf(
        MOOD_VERY_SAD to "Very Sad",
        MOOD_SAD to "Sad",
        MOOD_NEUTRAL to "Neutral",
        MOOD_HAPPY to "Happy",
        MOOD_VERY_HAPPY to "Very Happy"
    )
    
    // Severity Constants
    const val SEVERITY_MIN = 1
    const val SEVERITY_MAX = 10
    const val HOLD_DURATION_MAX_SECONDS = 5.0f
    const val TAP_THRESHOLD_SECONDS = 0.5f
    const val SEVERITY_MULTIPLIER = 2.0f
    
    // Graph Filter Presets (milliseconds)
    val FILTER_PRESETS = mapOf(
        "8_hours" to ("8 Hours" to 8L * 60 * 60 * 1000),
        "12_hours" to ("12 Hours" to 12L * 60 * 60 * 1000),
        "24_hours" to ("24 Hours" to 24L * 60 * 60 * 1000),
        "1_week" to ("1 Week" to 7L * 24 * 60 * 60 * 1000),
        "1_month" to ("1 Month" to 30L * 24 * 60 * 60 * 1000),
        "1_year" to ("1 Year" to 365L * 24 * 60 * 60 * 1000)
    )
    
    const val DEFAULT_FILTER_KEY = "1_week"
    
    // Color Palette
    const val COLOR_PRIMARY_BLUE = 0xFF007BFF
    const val COLOR_BACKGROUND_LIGHT = 0xFFF8F9FA
    const val COLOR_SURFACE_WHITE = 0xFFFFFFFF
    const val COLOR_ACCENT_GREEN = 0xFF28A745
    const val COLOR_NEGATIVE_RED = 0xFFDC3545
    const val COLOR_NEUTRAL_GREY = 0xFF6C757D
    const val COLOR_SHAKE_GROWTH = 0xFFFFC107
}