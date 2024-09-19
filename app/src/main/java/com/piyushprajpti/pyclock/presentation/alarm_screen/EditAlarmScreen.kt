package com.piyushprajpti.pyclock.presentation.alarm_screen

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.data.local_storage.alarm.AlarmData
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmViewModel
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.DateSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.FutureSelectableDates
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.TimeSelector
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.covertDateTimeToMillis
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.getDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAlarmScreen(
    alarmId: Int?,
    redirectToAlarmScreen: () -> Unit,
    alarmViewModel: AlarmViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tomorrowMillis = System.currentTimeMillis() + 86400000

    var initialData by remember {
        mutableStateOf(Triple(6, 0, tomorrowMillis))
    }

    LaunchedEffect(alarmId) {
        if (alarmId != null) {
            val alarmData = alarmViewModel.getAlarmDataById(alarmId)
            initialData = getDateTime(alarmData.time)
        }
    }

    var isValueChanged by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(initialData) {
        isValueChanged = true
    }

    val timePickerState = if (isValueChanged) {
        rememberTimePickerState(initialData.first, initialData.second)
    } else {
        rememberTimePickerState(initialData.first, initialData.second)
    }

    val datePickerState = if (isValueChanged) {
        rememberDatePickerState(
            initialSelectedDateMillis = initialData.third,
            selectableDates = FutureSelectableDates()
        )
    } else {
        rememberDatePickerState(
            initialSelectedDateMillis = initialData.third,
            selectableDates = FutureSelectableDates()
        )
    }

    fun onDeleteClick() {
        redirectToAlarmScreen()
        if (alarmId != null) {
            alarmViewModel.deleteAlarm(alarmId)
        }
    }

    fun onCancelClick() {
        redirectToAlarmScreen()
    }

    fun onSaveClick() {
        val currentTimeMillis = System.currentTimeMillis()
        val dateTimeMillis = covertDateTimeToMillis(
            dateMillis = datePickerState.selectedDateMillis ?: 0L,
            hourMillis = timePickerState.hour,
            minuteMillis = timePickerState.minute
        )

        if (dateTimeMillis <= currentTimeMillis) {
            Toast.makeText(context, "Can't set alarm for time in past", Toast.LENGTH_LONG).show()
        } else {
            alarmViewModel.scheduleAlarm(
                if (alarmId != null) AlarmData(alarmId, true, dateTimeMillis)
                else AlarmData(dateTimeMillis.hashCode(), true, dateTimeMillis)
            )
            redirectToAlarmScreen()
        }
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