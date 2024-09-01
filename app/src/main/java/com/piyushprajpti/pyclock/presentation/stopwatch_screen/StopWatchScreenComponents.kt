package com.piyushprajpti.pyclock.presentation.stopwatch_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class LapData(
    val lapCount: Int,
    val lapTime: String,
    val totalTime: String
)

fun formatElapsedTime(elapsedMillis: Long): String {
    val totalSeconds = (elapsedMillis + 1000) / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

@Composable
fun LapSectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Count",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
            modifier = Modifier.weight(0.26f)
        )

        Text(
            text = "Lap Time",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
            modifier = Modifier.weight(0.37f)
        )

        Text(
            text = "Total Time",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
            modifier = Modifier.weight(0.37f)
        )
    }

    HorizontalDivider(modifier = Modifier.fillMaxWidth())
}

@Composable
fun LapCard(
    lapCount: Int,
    lapTime: String,
    totalTime: String
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = lapCount.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            modifier = Modifier.weight(0.26f)
        )
        Text(
            text = lapTime,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.weight(0.37f)
        )
        Text(
            text = totalTime,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),

            modifier = Modifier.weight(0.37f)
        )
    }
}