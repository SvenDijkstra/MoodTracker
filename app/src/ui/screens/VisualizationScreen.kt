package com.aura.moodtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.moodtracker.ui.components.MoodGraph
import com.aura.moodtracker.ui.theme.BackgroundLight
import com.aura.moodtracker.ui.theme.PrimaryBlue
import com.aura.moodtracker.ui.viewmodels.VisualizationViewModel
import com.aura.moodtracker.utils.Constants
import com.aura.moodtracker.utils.ExportUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VisualizationScreen(
    viewModel: VisualizationViewModel = hiltViewModel()
) {
    val selectedFilterKey by viewModel.selectedFilterKey.collectAsState()
    val moodLogs by viewModel.moodLogs.collectAsState()
    val customStartTime by viewModel.customStartTime.collectAsState()
    val customEndTime by viewModel.customEndTime.collectAsState()
    
    var showCustomRangePicker by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Title
        Text(
            text = "Mood Trends",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )
        
        // Filter Buttons
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Constants.FILTER_PRESETS.toList()) { (key, value) ->
                FilterChip(
                    selected = selectedFilterKey == key,
                    onClick = { viewModel.selectPresetFilter(key) },
                    label = { Text(value.first) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedFilterKey == "custom",
                    onClick = { showCustomRangePicker = true },
                    label = { Text("Custom") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        
        // Graph
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            MoodGraph(
                moodLogs = moodLogs,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
        
        // Export Button
        Button(
            onClick = { showExportDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Export Data")
        }
    }
    
    // Custom Range Picker Dialog
    if (showCustomRangePicker) {
        CustomRangePickerDialog(
            onDismiss = { showCustomRangePicker = false },
            onConfirm = { start, end ->
                viewModel.selectCustomRange(start, end)
                showCustomRangePicker = false
            }
        )
    }
    
    // Export Dialog
    if (showExportDialog) {
        ExportDialog(
            onDismiss = { showExportDialog = false },
            onExport = { format ->
                coroutineScope.launch {
                    val context = LocalContext.current
                    ExportUtils.exportMoodData(
                        context = /* context */,
                        moodLogs = moodLogs,
                        format = format
                    )
                }
                showExportDialog = false
            }
        )
    }
}

@Composable
fun CustomRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long, Long) -> Unit
) {
    var startDate by remember { mutableStateOf(Calendar.getInstance()) }
    var endDate by remember { mutableStateOf(Calendar.getInstance()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date Range") },
        text = {
            Column {
                // Simplified date picker implementation
                // In production, use proper date picker libraries
                Text("Start Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(startDate.time)}")
                Text("End Date: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(endDate.time)}")
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                onConfirm(startDate.timeInMillis, endDate.timeInMillis)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ExportDialog(
    onDismiss: () -> Unit,
    onExport: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Format") },
        text = {
            Column {
                listOf("CSV", "JSON", "TXT").forEach { format ->
                    TextButton(
                        onClick = { onExport(format) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(format)
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}