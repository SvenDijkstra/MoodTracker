package com.aura.moodtracker.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.moodtracker.ui.components.MoodButton
import com.aura.moodtracker.ui.theme.BackgroundLight
import com.aura.moodtracker.ui.viewmodels.MoodEntryViewModel
import com.aura.moodtracker.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MoodEntryScreen(
    viewModel: MoodEntryViewModel = hiltViewModel()
) {
    val showConfirmation by viewModel.showConfirmation.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "How are you feeling?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                for (mood in 0..4) {
                    MoodButton(
                        moodValue = mood,
                        onMoodLogged = { holdDuration ->
                            viewModel.logMood(mood, holdDuration)
                        }
                    )
                }
            }
            
            Text(
                text = "Tap to log mood\nHold for intensity",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        if (showConfirmation) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Mood logged!",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
            }
        }
    }
}