package com.piyushprajpti.pyclock.presentation.alarm_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.data.local_storage.alarm.AlarmData
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmCard
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.AlarmViewModel
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.NoAlarmDisplay
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun AlarmScreen(
    onAlarmCardClick: (alarmId: Int?) -> Unit,
    alarmViewModel: AlarmViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val alarmsList = remember {
        mutableStateOf<List<AlarmData>>(emptyList())
    }

    LaunchedEffect(Unit) {
        alarmsList.value = alarmViewModel.getAllAlarms().reversed()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            if (alarmsList.value.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.dp else 150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        NoAlarmDisplay()
                    }
                }
            } else {
                items(alarmsList.value) { alarmData ->
                    AlarmCard(alarmData = alarmData, onClick = { onAlarmCardClick(alarmData.id) })
                }
            }
        }

        // add alarm FAB
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add alarm",
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .align(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) Alignment.BottomEnd else Alignment.BottomCenter)
                .background(VioletBlue, CircleShape)
                .clip(CircleShape)
                .size(60.dp)
                .clickable { onAlarmCardClick(null) }
                .padding(10.dp)
        )

    }
}