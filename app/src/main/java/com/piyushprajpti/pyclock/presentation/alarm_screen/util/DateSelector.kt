package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    datePickerState: DatePickerState
) {
    val showDialogBox = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tomorrow",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 40.sp,
        )

        Icon(imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = "set date",
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
            content = {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = "Select Alarm Time",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    showModeToggle = false
                )
            },
            onCancelClick = {
                showDialogBox.value = false
            },
            onOkClick = {
                showDialogBox.value = false
            }
        )
    }
}