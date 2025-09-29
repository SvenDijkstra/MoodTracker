package com.aura.moodtracker.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.aura.moodtracker.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mood_preferences")

class PreferencesManager(private val context: Context) {
    
    companion object {
        val LAST_FILTER_KEY = stringPreferencesKey("last_filter_key")
        val CUSTOM_RANGE_START = longPreferencesKey("custom_range_start")
        val CUSTOM_RANGE_END = longPreferencesKey("custom_range_end")
    }
    
    val lastFilterKey: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_FILTER_KEY] ?: Constants.DEFAULT_FILTER_KEY
        }
    
    val customRangeStart: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOM_RANGE_START]
        }
    
    val customRangeEnd: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[CUSTOM_RANGE_END]
        }
    
    suspend fun saveLastFilterKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_FILTER_KEY] = key
        }
    }
    
    suspend fun saveCustomRange(startTime: Long?, endTime: Long?) {
        context.dataStore.edit { preferences ->
            if (startTime != null) {
                preferences[CUSTOM_RANGE_START] = startTime
            } else {
                preferences.remove(CUSTOM_RANGE_START)
            }
            if (endTime != null) {
                preferences[CUSTOM_RANGE_END] = endTime
            } else {
                preferences.remove(CUSTOM_RANGE_END)
            }
        }
    }
}