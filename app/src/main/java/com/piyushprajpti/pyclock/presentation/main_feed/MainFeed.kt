package com.piyushprajpti.pyclock.presentation.main_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainFeed(
    onSettingClick: () -> Unit
) {

    val activeButton = remember {
        mutableStateOf(0)
    }

    val topBarTitle = remember {
        mutableStateOf("Clock")
    }

    val tbt = when(activeButton.value) {
         0 -> "Clock"
            1 -> "Alarm"
        2 -> "Stop Watch"
        3 -> "Timer"
        else -> "Other"
    }

    Scaffold(
        topBar = {
            TopBar(title = tbt, onSettingClick = { onSettingClick() })
        },

        bottomBar = {
            BottomBar(
                iconsList = listOf(
                    IconData(
                        icon = Icons.Outlined.WatchLater,
                        iconName = "clock"
                    ),
                    IconData(
                        icon = Icons.Outlined.Alarm,
                        iconName = "alarm"
                    ),
                    IconData(
                        icon = Icons.Outlined.HourglassTop,
                        iconName = "stopwatch"
                    ),
                    IconData(
                        icon = Icons.Outlined.Timer,
                        iconName = "timer"
                    ),

                    ),
                activeButton = activeButton,
                onClick = { activeButton.value = it }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {

        }
    }
}