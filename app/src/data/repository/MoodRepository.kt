package com.aura.moodtracker.data.repository

import com.aura.moodtracker.data.database.MoodLogDao
import com.aura.moodtracker.data.database.MoodLogEntity
import com.aura.moodtracker.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodRepository @Inject constructor(
    private val moodLogDao: MoodLogDao,
    private val preferencesManager: PreferencesManager
) {
    suspend fun insertMoodLog(timestamp: Long, moodValue: Int, severity: Int) {
        moodLogDao.insertMoodLog(
            MoodLogEntity(
                timestamp = timestamp,
                mood_value = moodValue,
                severity = severity
            )
        )
    }
    
    fun getMoodLogsBetween(startTime: Long, endTime: Long): Flow<List<MoodLogEntity>> {
        return moodLogDao.getMoodLogsBetween(startTime, endTime)
    }
    
    fun getDailyAverageMoodLogs(startTime: Long, endTime: Long): Flow<List<MoodLogEntity>> {
        return moodLogDao.getDailyAverageMoodLogs(startTime, endTime)
    }
    
    fun getAllMoodLogs(): Flow<List<MoodLogEntity>> {
        return moodLogDao.getAllMoodLogs()
    }
    
    fun getLastFilterKey(): Flow<String> = preferencesManager.lastFilterKey
    
    suspend fun saveLastFilterKey(key: String) {
        preferencesManager.saveLastFilterKey(key)
    }
    
    fun getCustomRange(): Pair<Flow<Long?>, Flow<Long?>> {
        return Pair(preferencesManager.customRangeStart, preferencesManager.customRangeEnd)
    }
    
    suspend fun saveCustomRange(startTime: Long?, endTime: Long?) {
        preferencesManager.saveCustomRange(startTime, endTime)
    }
}