package com.aura.moodtracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.moodtracker.data.database.MoodLogEntity
import com.aura.moodtracker.ui.theme.*
import com.aura.moodtracker.utils.Constants

@Composable
fun MoodGraph(
    moodLogs: List<MoodLogEntity>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    
    if (moodLogs.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No mood data available", color = NeutralGrey)
        }
        return
    }
    
    Canvas(modifier = modifier) {
        val padding = 60.dp.toPx()
        val graphWidth = size.width - padding * 2
        val graphHeight = size.height - padding * 2
        
        // Draw axes
        drawLine(
            color = Color.LightGray,
            start = Offset(padding, padding),
            end = Offset(padding, size.height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.LightGray,
            start = Offset(padding, size.height - padding),
            end = Offset(size.width - padding, size.height - padding),
            strokeWidth = 2f
        )
        
        // Draw Y-axis labels (mood labels)
        val yLabels = listOf("Very Sad", "Sad", "Neutral", "Happy", "Very Happy")
        yLabels.forEachIndexed { index, label ->
            val y = size.height - padding - (index * graphHeight / 4)
            val textLayoutResult = textMeasurer.measure(
                text = label,
                style = TextStyle(fontSize = 12.sp, color = NeutralGrey)
            )
            drawText(
                textLayoutResult,
                topLeft = Offset(padding - textLayoutResult.size.width - 10, y - textLayoutResult.size.height / 2)
            )
        }
        
        // Draw horizontal grid lines
        for (i in 0..4) {
            val y = size.height - padding - (i * graphHeight / 4)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(padding, y),
                end = Offset(size.width - padding, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f))
            )
        }
        
        // Plot mood data
        if (moodLogs.size > 1) {
            val path = Path()
            val points = mutableListOf<Offset>()
            
            val minTime = moodLogs.minOf { it.timestamp }
            val maxTime = moodLogs.maxOf { it.timestamp }
            val timeRange = (maxTime - minTime).toFloat()
            
            moodLogs.forEachIndexed { index, log ->
                val x = if (timeRange > 0) {
                    padding + ((log.timestamp - minTime) / timeRange) * graphWidth
                } else {
                    padding + graphWidth / 2
                }
                val y = size.height - padding - (log.mood_value / 4f) * graphHeight
                
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
                points.add(Offset(x, y))
            }
            
            // Draw the line
            drawPath(
                path = path,
                color = PrimaryBlue,
                style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            
            // Draw points with severity indication (size based on severity)
            points.forEachIndexed { index, point ->
                val severity = moodLogs[index].severity
                val radius = 3f + (severity / 10f) * 5f // Radius from 3 to 8 based on severity
                
                drawCircle(
                    color = PrimaryBlue,
                    radius = radius,
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = radius - 2f,
                    center = point
                )
                drawCircle(
                    color = PrimaryBlue,
                    radius = radius - 3f,
                    center = point
                )
            }
        } else if (moodLogs.size == 1) {
            // Draw single point
            val log = moodLogs[0]
            val x = size.width / 2
            val y = size.height - padding - (log.mood_value / 4f) * graphHeight
            val severity = log.severity
            val radius = 5f + (severity / 10f) * 5f
            
            drawCircle(
                color = PrimaryBlue,
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}