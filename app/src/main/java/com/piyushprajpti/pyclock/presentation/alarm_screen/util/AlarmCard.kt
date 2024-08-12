package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun AlarmCard(
    alarmData: AlarmData,
    onClick: () -> Unit
) {
    val isChecked = remember {
        mutableStateOf(alarmData.isOn)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = alarmData.time,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isChecked.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (alarmData.isAM) "AM" else "PM",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isChecked.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

            }

            Text(
                text = alarmData.date,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (isChecked.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
            )
        }

        Switch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = !isChecked.value },
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                checkedTrackColor = VioletBlue,
                uncheckedThumbColor = Color.Gray,
                checkedThumbColor = Color.White
            )
        )
    }
}