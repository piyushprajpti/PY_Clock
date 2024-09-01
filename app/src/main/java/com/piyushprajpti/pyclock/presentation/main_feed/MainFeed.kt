package com.piyushprajpti.pyclock.presentation.main_feed

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.piyushprajpti.pyclock.presentation.alarm_screen.AlarmScreen
import com.piyushprajpti.pyclock.presentation.clock_screen.ClockScreen
import com.piyushprajpti.pyclock.presentation.stopwatch_screen.StopWatchScreen
import com.piyushprajpti.pyclock.presentation.timer_screen.TimerScreen
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.service.stopwatch.StopwatchState
import com.piyushprajpti.pyclock.service.timer.TimerService
import com.piyushprajpti.pyclock.service.timer.TimerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainFeed(
    selectedTheme: Int,
    stopWatchService: StopWatchService,
    timerService: TimerService,
    onSettingClick: () -> Unit,
    onAlarmCardClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

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