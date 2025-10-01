package com.aura.moodtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.moodtracker.data.database.MoodLogEntity
import com.aura.moodtracker.data.repository.MoodRepository
import com.aura.moodtracker.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VisualizationViewModel @Inject constructor(
    private val repository: MoodRepository
) : ViewModel() {
    
    private val _selectedFilterKey = MutableStateFlow(Constants.DEFAULT_FILTER_KEY)
    val selectedFilterKey: StateFlow<String> = _selectedFilterKey
    
    private val _customStartTime = MutableStateFlow<Long?>(null)
    val customStartTime: StateFlow<Long?> = _customStartTime
    
    private val _customEndTime = MutableStateFlow<Long?>(null)
    val customEndTime: StateFlow<Long?> = _customEndTime
    
    private val _moodLogs = MutableStateFlow<List<MoodLogEntity>>(emptyList())
    val moodLogs: StateFlow<List<MoodLogEntity>> = _moodLogs
    
    init {
        loadLastFilter()
        loadMoodData()
    }
    
    private fun loadLastFilter() {
        viewModelScope.launch {
            repository.getLastFilterKey().collect { key ->
                _selectedFilterKey.value = key
                if (!key.startsWith("custom")) {
                    loadMoodData()
                } else {
                    loadCustomRange()
                }
            }
        }
    }
    
    private fun loadCustomRange() {
        viewModelScope.launch {
            val (startFlow, endFlow) = repository.getCustomRange()
            combine(startFlow, endFlow) { start, end ->
                _customStartTime.value = start
                _customEndTime.value = end
                if (start != null && end != null) {
                    loadMoodDataForRange(start, end)
                }
            }.collect()
        }
    }
    
    fun selectPresetFilter(filterKey: String) {
        viewModelScope.launch {
            _selectedFilterKey.value = filterKey
            repository.saveLastFilterKey(filterKey)
            loadMoodData()
        }
    }
    
    fun selectCustomRange(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            _customStartTime.value = startTime
            _customEndTime.value = endTime
            _selectedFilterKey.value = "custom"
            repository.saveLastFilterKey("custom")
            repository.saveCustomRange(startTime, endTime)
            loadMoodDataForRange(startTime, endTime)
        }
    }
    
    private fun loadMoodData() {
        val filterKey = _selectedFilterKey.value
        if (filterKey == "custom") {
            val start = _customStartTime.value
            val end = _customEndTime.value
            if (start != null && end != null) {
                loadMoodDataForRange(start, end)
            }
        } else {
            val milliseconds = Constants.FILTER_PRESETS[filterKey]?.second ?: return
            val endTime = System.currentTimeMillis()
            val startTime = endTime - milliseconds
            loadMoodDataForRange(startTime, endTime)
        }
    }
    
    private fun loadMoodDataForRange(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            val duration = endTime - startTime
            val dayInMillis = 24L * 60 * 60 * 1000
            
            val flow = if (duration >= 7 * dayInMillis) {
                repository.getDailyAverageMoodLogs(startTime, endTime)
            } else {
                repository.getMoodLogsBetween(startTime, endTime)
            }
            
            flow.collect { logs ->
                _moodLogs.value = logs
            }
        }
    }
    
    fun exportData(format: String): String {
        // Implementation moved to ExportUtils
        return ""
    }
}