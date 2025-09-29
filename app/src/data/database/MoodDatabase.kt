package com.aura.moodtracker.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [MoodLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun moodLogDao(): MoodLogDao
    
    companion object {
        @Volatile
        private var INSTANCE: MoodDatabase? = null
        
        fun getDatabase(context: Context): MoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    "mood_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}