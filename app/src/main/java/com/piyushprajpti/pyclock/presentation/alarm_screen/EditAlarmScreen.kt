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
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmData
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmViewModel
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.DateSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.FutureSelectableDates
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.ScheduleAlarmData
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.TimeSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.dateFormatter
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.timeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun EditAlarmScreen(
    redirectToAlarmScreen: (alarmData: AlarmData) -> Unit,
    alarmViewModel: AlarmViewModel = hiltViewModel()
) {
    val tomorrowMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000

    val timePickerState = rememberTimePickerState()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = tomorrowMillis,
        selectableDates = FutureSelectableDates()
    )

    fun calculateScheduleTimeMillis(dateMillis: Long, hourMillis: Int, minuteMillis: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateMillis
            set(Calendar.HOUR_OF_DAY, hourMillis)
            set(Calendar.MINUTE, minuteMillis)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }

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
        val scheduleTimeMillis = calculateScheduleTimeMillis(
            dateMillis = datePickerState.selectedDateMillis ?: 0L,
            hourMillis = timePickerState.hour,
            minuteMillis = timePickerState.minute
        )

        alarmViewModel.scheduleAlarm(scheduleTimeMillis)
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