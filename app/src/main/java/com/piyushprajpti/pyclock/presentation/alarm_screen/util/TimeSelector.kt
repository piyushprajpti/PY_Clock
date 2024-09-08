package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.DialogBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelector(
    timePickerState: TimePickerState
) {

    val showDialogBox = remember {
        mutableStateOf(false)
    }

    val (initialHour, initialMinute, initialPeriod) = timeFormatter(
        timePickerState.hour,
        timePickerState.minute
    )

    val hour = remember {
        mutableStateOf(initialHour)
    }

    val minute = remember {
        mutableStateOf(initialMinute)
    }

    val period = remember {
        mutableStateOf(initialPeriod)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "${hour.value}:${minute.value}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = period.value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Icon(imageVector = Icons.Default.AccessTime,
            contentDescription = "set time",
            tint = VioletBlue,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
                .clickable { showDialogBox.value = true }
                .padding(6.dp)
        )
    }

    if (showDialogBox.value) {
        DialogBox(
            title = "Select Alarm Time",
            content = {
                TimePicker(state = timePickerState)
            },
            onCancelClick = {
                showDialogBox.value = false
            },
            onOkClick = {
                val temp = timeFormatter(timePickerState.hour, timePickerState.minute)
                hour.value = temp.first
                minute.value = temp.second
                period.value = temp.third
                showDialogBox.value = false
            }
        )
    }
}