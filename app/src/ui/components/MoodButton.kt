package com.aura.moodtracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.moodtracker.ui.theme.*
import com.aura.moodtracker.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MoodButton(
    moodValue: Int,
    onMoodLogged: (Float) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var holdDuration by remember { mutableStateOf(0f) }
    var holdJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    // Calculate scale based on hold duration
    val scale by animateFloatAsState(
        targetValue = if (isPressed) {
            1f + (holdDuration / Constants.HOLD_DURATION_MAX_SECONDS) * 0.5f
        } else 1f,
        animationSpec = tween(100)
    )
    
    // Shake animation for holding
    val infiniteTransition = rememberInfiniteTransition()
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPressed && holdDuration > Constants.TAP_THRESHOLD_SECONDS) 5f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(50),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Determine emoji and color
    val (emoji, label, baseColor) = when (moodValue) {
        Constants.MOOD_VERY_SAD -> {
            val finalEmoji = if (isPressed && holdDuration >= Constants.HOLD_DURATION_MAX_SECONDS) "😡" else "😢"
            Triple(finalEmoji, "Very Sad", NegativeRed)
        }
        Constants.MOOD_SAD -> Triple("😞", "Sad", Color(0xFFFF9800))
        Constants.MOOD_NEUTRAL -> Triple("😐", "Neutral", NeutralGrey)
        Constants.MOOD_HAPPY -> Triple("😊", "Happy", AccentGreen)
        Constants.MOOD_VERY_HAPPY -> {
            val finalEmoji = if (isPressed && holdDuration >= Constants.HOLD_DURATION_MAX_SECONDS) "🤩" else "😄"
            Triple(finalEmoji, "Very Happy", PrimaryBlue)
        }
        else -> Triple("😐", "Neutral", NeutralGrey)
    }
    
    val backgroundColor = if (isPressed && holdDuration > Constants.TAP_THRESHOLD_SECONDS) {
        ShakeGrowth
    } else {
        baseColor.copy(alpha = 0.1f)
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(x = shakeOffset.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(backgroundColor)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            holdDuration = 0f
                            
                            holdJob = coroutineScope.launch {
                                while (isPressed && holdDuration < Constants.HOLD_DURATION_MAX_SECONDS) {
                                    delay(50)
                                    holdDuration += 0.05f
                                }
                            }
                            
                            tryAwaitRelease()
                            
                            holdJob?.cancel()
                            isPressed = false
                            onMoodLogged(holdDuration)
                            holdDuration = 0f
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}