package com.aura.moodtracker.di

import android.content.Context
import com.aura.moodtracker.data.database.MoodDatabase
import com.aura.moodtracker.data.database.MoodLogDao
import com.aura.moodtracker.data.preferences.PreferencesManager
import com.aura.moodtracker.data.repository.MoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMoodDatabase(
        @ApplicationContext context: Context
    ): MoodDatabase = MoodDatabase.getDatabase(context)
    
    @Provides
    @Singleton
    fun provideMoodLogDao(database: MoodDatabase): MoodLogDao = database.moodLogDao()
    
    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager = PreferencesManager(context)
    
    @Provides
    @Singleton
    fun provideMoodRepository(
        moodLogDao: MoodLogDao,
        preferencesManager: PreferencesManager
    ): MoodRepository = MoodRepository(moodLogDao, preferencesManager)
}