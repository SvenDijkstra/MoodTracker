package com.aura.moodtracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodLogDao {
    @Insert
    suspend fun insertMoodLog(moodLog: MoodLogEntity)
    
    @Query("SELECT * FROM mood_entries WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp ASC")
    fun getMoodLogsBetween(startTime: Long, endTime: Long): Flow<List<MoodLogEntity>>
    
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodLogs(): Flow<List<MoodLogEntity>>
    
    @Query("""
        SELECT 
            CAST((timestamp / 86400000) * 86400000 AS INTEGER) as timestamp,
            CAST(AVG(mood_value) AS INTEGER) as mood_value,
            CAST(AVG(severity) AS INTEGER) as severity,
            0 as id
        FROM mood_entries
        WHERE timestamp BETWEEN :startTime AND :endTime
        GROUP BY timestamp / 86400000
        ORDER BY timestamp ASC
    """)
    fun getDailyAverageMoodLogs(startTime: Long, endTime: Long): Flow<List<MoodLogEntity>>
}