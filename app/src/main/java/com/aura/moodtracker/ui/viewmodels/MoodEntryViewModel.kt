package com.aura.moodtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.moodtracker.data.repository.MoodRepository
import com.aura.moodtracker.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

@HiltViewModel
class MoodEntryViewModel @Inject constructor(
    private val repository: MoodRepository
) : ViewModel() {
    
    private val _showConfirmation = MutableStateFlow(false)
    val showConfirmation: StateFlow<Boolean> = _showConfirmation
    
    fun logMood(moodValue: Int, holdDurationSeconds: Float) {
        viewModelScope.launch {
            val severity = calculateSeverity(holdDurationSeconds)
            repository.insertMoodLog(
                timestamp = System.currentTimeMillis(),
                moodValue = moodValue,
                severity = severity
            )
            
            _showConfirmation.value = true
            kotlinx.coroutines.delay(2000)
            _showConfirmation.value = false
        }
    }
    
    private fun calculateSeverity(holdDurationSeconds: Float): Int {
        return if (holdDurationSeconds < Constants.TAP_THRESHOLD_SECONDS) {
            Constants.SEVERITY_MIN
        } else {
            val calculatedSeverity = (holdDurationSeconds * Constants.SEVERITY_MULTIPLIER).roundToInt()
            min(calculatedSeverity, Constants.SEVERITY_MAX)
        }
    }
}