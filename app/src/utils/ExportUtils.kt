package com.aura.moodtracker.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.aura.moodtracker.data.database.MoodLogEntity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {
    
    suspend fun exportMoodData(
        context: Context,
        moodLogs: List<MoodLogEntity>,
        format: String
    ) = withContext(Dispatchers.IO) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "mood_data_$timestamp"
            val fileExtension = format.lowercase()
            
            val content = when (format.uppercase()) {
                "CSV" -> generateCsv(moodLogs)
                "JSON" -> generateJson(moodLogs)
                "TXT" -> generateTxt(moodLogs)
                else -> throw IllegalArgumentException("Unknown format: $format")
            }
            
            val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ use MediaStore
                saveToMediaStore(context, fileName, fileExtension, content)
            } else {
                // For older versions, use external storage
                saveToExternalStorage(fileName, fileExtension, content)
            }
            
            withContext(Dispatchers.Main) {
                if (success) {
                    Toast.makeText(
                        context,
                        "Mood data exported to Downloads/$fileName.$fileExtension",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Export failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Export failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun generateCsv(moodLogs: List<MoodLogEntity>): String {
        val builder = StringBuilder()
        builder.appendLine("timestamp,mood_value,severity")
        
        moodLogs.forEach { log ->
            builder.appendLine("${log.timestamp},${log.mood_value},${log.severity}")
        }
        
        return builder.toString()
    }
    
    private fun generateJson(moodLogs: List<MoodLogEntity>): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val exportData = moodLogs.map { log ->
            mapOf(
                "timestamp" to log.timestamp,
                "mood_value" to log.mood_value,
                "severity" to log.severity
            )
        }
        return gson.toJson(exportData)
    }
    
    private fun generateTxt(moodLogs: List<MoodLogEntity>): String {
        val builder = StringBuilder()
        builder.appendLine("timestamp|mood_value|severity")
        
        moodLogs.forEach { log ->
            builder.appendLine("${log.timestamp}|${log.mood_value}|${log.severity}")
        }
        
        return builder.toString()
    }
    
    private fun saveToMediaStore(
        context: Context,
        fileName: String,
        extension: String,
        content: String
    ): Boolean {
        return try {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.$extension")
                put(MediaStore.Downloads.MIME_TYPE, getMimeType(extension))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
            }
            
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private fun saveToExternalStorage(
        fileName: String,
        extension: String,
        content: String
    ): Boolean {
        return try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$fileName.$extension"
            )
            file.writeText(content)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private fun getMimeType(extension: String): String {
        return when (extension.lowercase()) {
            "csv" -> "text/csv"
            "json" -> "application/json"
            "txt" -> "text/plain"
            else -> "text/plain"
        }
    }
}