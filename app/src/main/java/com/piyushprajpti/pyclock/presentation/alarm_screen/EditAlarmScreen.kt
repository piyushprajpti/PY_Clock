package com.piyushprajpti.pyclock.presentation.alarm_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmData
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.DateSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.FutureSelectableDates
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.TimeSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.dateFormatter
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.timeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun EditAlarmScreen(
    redirectToAlarmScreen: (alarmData: AlarmData) -> Unit
) {
    val tomorrowMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000

    val timePickerState = rememberTimePickerState()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = tomorrowMillis,
        selectableDates = FutureSelectableDates()
    )

    fun onDeleteClick() {
        redirectToAlarmScreen(
            AlarmData(
                true,
                timeFormatter(timePickerState.hour, timePickerState.minute),
                dateFormatter(datePickerState.selectedDateMillis)
            )
        )
    }

    fun onCancelClick() {
        redirectToAlarmScreen(
            AlarmData(
                true,
                timeFormatter(timePickerState.hour, timePickerState.minute),
                dateFormatter(datePickerState.selectedDateMillis)
            )
        )
    }

    fun onSaveClick() {
        redirectToAlarmScreen(
            AlarmData(
                true,
                timeFormatter(timePickerState.hour, timePickerState.minute),
                dateFormatter(datePickerState.selectedDateMillis)
            )
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit Alarm",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "delete",
                    tint = Color.Red,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(45.dp)
                        .clickable { onDeleteClick() }
                        .padding(8.dp)
                )
            }
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onCancelClick() }
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                )

                Text(
                    text = "",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        .width(1.dp)
                        .height(25.dp)
                )

                Text(
                    text = "Save",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSaveClick() }
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(20.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Alarm Time",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            item {
                TimeSelector(
                    timePickerState = timePickerState
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Alarm Date",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            item {
                DateSelector(
                    datePickerState = datePickerState
                )
            }
        }
    }
}