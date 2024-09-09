package com.piyushprajpti.pyclock.presentation.main_feed

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.piyushprajpti.pyclock.presentation.alarm_screen.AlarmScreen
import com.piyushprajpti.pyclock.presentation.clock_screen.ClockScreen
import com.piyushprajpti.pyclock.presentation.stopwatch_screen.StopWatchScreen
import com.piyushprajpti.pyclock.presentation.timer_screen.TimerScreen
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.service.stopwatch.StopwatchState
import com.piyushprajpti.pyclock.service.timer.TimerService
import com.piyushprajpti.pyclock.service.timer.TimerState
import com.piyushprajpti.pyclock.ui.theme.ErrorRed
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    SideEffect {
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    var isNotificationPermissionDialogVisible by remember {
        mutableStateOf(hasNotificationPermission)
    }

    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }

    val isDarkTheme = when (selectedTheme) {
        1 -> isSystemInDarkTheme()
        2 -> false
        else -> true
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
        if (!isNotificationPermissionDialogVisible) {
            Dialog(
                onDismissRequest = {
                    isNotificationPermissionDialogVisible = !isNotificationPermissionDialogVisible
                }) {
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Attention User!!!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textDecoration = TextDecoration.Underline,
                        color = ErrorRed
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Please grant notification permission for the app to function properly",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Go To Settings",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .clickable {
                                context.startActivity(settingsIntent)
                            }
                            .padding(10.dp)
                    )
                }
            }
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