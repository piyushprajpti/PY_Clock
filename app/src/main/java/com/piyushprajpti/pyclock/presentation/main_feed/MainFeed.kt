package com.piyushprajpti.pyclock.presentation.main_feed

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.piyushprajpti.pyclock.presentation.alarm_screen.AlarmScreen
import com.piyushprajpti.pyclock.presentation.clock_screen.ClockScreen
import com.piyushprajpti.pyclock.presentation.stopwatch_screen.StopWatchScreen
import com.piyushprajpti.pyclock.presentation.timer_screen.TimerScreen
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.service.stopwatch.StopwatchState
import com.piyushprajpti.pyclock.service.timer.TimerService
import com.piyushprajpti.pyclock.service.timer.TimerState
import com.piyushprajpti.pyclock.util.NotificationPermissionDialogBox
import kotlinx.coroutines.launch

@Composable
fun MainFeed(
    selectedTheme: Int,
    stopWatchService: StopWatchService,
    timerService: TimerService,
    onSettingClick: () -> Unit,
    onAlarmCardClick: (alarmId: Int?) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    var hasRequestedPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasRequestedPermission = true
            hasNotificationPermission = isGranted
        }
    )

    SideEffect {
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    var showNotificationPermissionDialog by remember {
        mutableStateOf(false)
    }

    if (hasRequestedPermission && !hasNotificationPermission) {
        showNotificationPermissionDialog = true
    }

    val isDarkTheme = when (selectedTheme) {
        2 -> false
        3 -> true
        else -> isSystemInDarkTheme()
    }

    val stopwatchCondition =
        stopWatchService.currentState.value == StopwatchState.Started || stopWatchService.currentState.value == StopwatchState.Paused
    val timerCondition =
        timerService.currentState.value == TimerState.Started || timerService.currentState.value == TimerState.Paused

    val initialPage =
        if (stopwatchCondition) 2
        else if (timerCondition) 3
        else 0

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 4 })

    val topBarTitle = when (pagerState.currentPage) {
        0 -> "Clock"
        1 -> "Alarm"
        2 -> "Stop Watch"
        3 -> "Timer"
        else -> "Other"
    }

    fun onBottomBarIconClick(i: Int) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(i)
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = topBarTitle, onSettingClick = { onSettingClick() })
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
                activeButton = pagerState.currentPage,
                onClick = { onBottomBarIconClick(it) }
            )
        }
    ) {
        if (showNotificationPermissionDialog) {
            NotificationPermissionDialogBox(
                onDismissRequest = {
                    showNotificationPermissionDialog = false
                }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) { page ->

            when (page) {
                0 -> ClockScreen(isDarkTheme = isDarkTheme)
                1 -> AlarmScreen(onAlarmCardClick)
                2 -> StopWatchScreen(isDarkTheme = isDarkTheme, stopWatchService = stopWatchService)
                3 -> TimerScreen(isDarkTheme = isDarkTheme, timerService = timerService)
            }
        }
    }
}