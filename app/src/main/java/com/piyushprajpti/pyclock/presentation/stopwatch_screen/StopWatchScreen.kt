package com.piyushprajpti.pyclock.presentation.stopwatch_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.data.local_storage.stopwatch.LapData
import com.piyushprajpti.pyclock.presentation.CommonViewModel
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchServiceIntents
import com.piyushprajpti.pyclock.service.stopwatch.StopwatchState
import com.piyushprajpti.pyclock.ui.theme.ErrorRed
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.ActionButton
import com.piyushprajpti.pyclock.util.CircularProgressCanvas
import com.piyushprajpti.pyclock.util.Constants
import com.piyushprajpti.pyclock.util.PlayButton
import com.piyushprajpti.pyclock.util.fix
import kotlinx.coroutines.delay

@Composable
fun StopWatchScreen(
    isDarkTheme: Boolean,
    stopWatchService: StopWatchService,
    commonViewModel: CommonViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val hours by stopWatchService.hours
    val minutes by stopWatchService.minutes
    val seconds by stopWatchService.seconds
    val currentState by stopWatchService.currentState

    var isStarted by remember {
        mutableStateOf(
            (currentState == StopwatchState.Started) || (currentState == StopwatchState.Paused)
        )
    }

    var shouldReset by remember { mutableStateOf(currentState == StopwatchState.Paused) }

    var isProgressBarActive by remember { mutableStateOf(currentState == StopwatchState.Started) }

    var progress by remember { mutableFloatStateOf(0f) }

    val durationMillis = 60 * 1000L

    val lapsList = remember {
        mutableStateOf(listOf<LapData>())
    }

    var lapCount by remember {
        mutableIntStateOf(0)
    }

    var previousLapTime by remember {
        mutableLongStateOf(0L)
    }

    LaunchedEffect(Unit) {
        if (currentState == StopwatchState.Idle) {
            commonViewModel.clearLapData()
        } else {
            val lapListFromVM = commonViewModel.getLapList()
            lapsList.value = lapListFromVM
            lapCount = if (lapListFromVM.isNotEmpty()) lapListFromVM.size else 0
            if (lapListFromVM.isNotEmpty()) {
                val lastLap = lapListFromVM.last()
                previousLapTime = parseTimeToMillis(lastLap.totalTime)
            }
        }
    }

    LaunchedEffect(lapsList.value) {
        commonViewModel.addLap(lapsList.value)
    }

    LaunchedEffect(isProgressBarActive) {
        if (isProgressBarActive) {
            while (isProgressBarActive) {
                val elapsedMillis =
                    hours.toLong() * 3600000L + minutes.toLong() * 60000L + seconds.toLong() * 1000L
                progress =
                    (elapsedMillis.toFloat() % durationMillis / durationMillis).coerceIn(0f, 1f)

                delay(33L)
            }
        }
    }

    fun onResetClick() {
        progress = 0f
        isStarted = false
        shouldReset = false
        lapsList.value = listOf()
        lapCount = 0
        previousLapTime = 0L

        StopWatchServiceIntents.triggerForegroundService(
            context = context, action = Constants.ACTION_SERVICE_CANCEL
        )
    }

    fun onLapClick() {
        lapCount += 1
        val currentElapsedMillis =
            hours.toLong() * 3600000L + minutes.toLong() * 60000L + seconds.toLong() * 1000L - 3L

        val newLapTime = currentElapsedMillis - previousLapTime - 1L
        previousLapTime = currentElapsedMillis

        val newLap = LapData(
            lapCount,
            formatElapsedTime(newLapTime),
            formatElapsedTime(currentElapsedMillis)
        )
        lapsList.value += newLap

    }


    LaunchedEffect(currentState) {
        if (currentState == StopwatchState.Idle) {
            onResetClick()
            commonViewModel.clearLapData()
        }
        shouldReset = currentState == StopwatchState.Paused
        isProgressBarActive = currentState == StopwatchState.Started
    }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$hours:$minutes:$seconds",
                    fontSize = 60.sp,
                    letterSpacing = 3.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (lapCount > 0) {
                    Column(modifier = Modifier.weight(0.7f)) {
                        LapSectionHeader()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(lapsList.value.reversed()) {
                                LapCard(
                                    lapCount = it.lapCount,
                                    lapTime = it.lapTime,
                                    totalTime = it.totalTime
                                )
                            }
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 5.dp)
                        .weight(0.3f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isStarted) {
                        ActionButton(
                            title = if (shouldReset) "Reset" else "Lap",
                            titleColor = MaterialTheme.colorScheme.primary,
                            backColor = MaterialTheme.colorScheme.secondary,
                            onClick = {
                                if (shouldReset) {
                                    onResetClick()
                                } else {
                                    onLapClick()
                                }
                            }
                        )

                        ActionButton(
                            title = if (shouldReset) "Resume" else "Pause",
                            titleColor = Color.White,
                            backColor = if (shouldReset) VioletBlue else ErrorRed,
                            onClick = {
                                StopWatchServiceIntents.triggerForegroundService(
                                    context = context,
                                    action = if (currentState == StopwatchState.Started) Constants.ACTION_SERVICE_STOP else Constants.ACTION_SERVICE_START
                                )
                                isProgressBarActive = shouldReset
                                shouldReset = !shouldReset
                            }
                        )
                    } else {
                        PlayButton {
                            isStarted = true
                            isProgressBarActive = true
                            StopWatchServiceIntents.triggerForegroundService(
                                context = context,
                                action = Constants.ACTION_SERVICE_START
                            )
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressCanvas(
                        isDarkTheme = isDarkTheme,
                        modifier = Modifier
                            .fillMaxSize(),
                        progress = progress
                    )

                    Text(
                        text = "$hours:$minutes:$seconds",
                        fontSize = 40.sp.fix,
                        letterSpacing = 3.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (lapCount > 0) {
                    LapSectionHeader()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(lapsList.value.reversed()) {
                            LapCard(
                                lapCount = it.lapCount,
                                lapTime = it.lapTime,
                                totalTime = it.totalTime
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isStarted) {
                    ActionButton(
                        title = if (shouldReset) "Reset" else "Lap",
                        titleColor = MaterialTheme.colorScheme.primary,
                        backColor = MaterialTheme.colorScheme.secondary,
                        onClick = {
                            if (shouldReset) {
                                onResetClick()
                            } else {
                                onLapClick()
                            }
                        }
                    )

                    ActionButton(
                        title = if (shouldReset) "Resume" else "Pause",
                        titleColor = Color.White,
                        backColor = if (shouldReset) VioletBlue else ErrorRed,
                        onClick = {
                            StopWatchServiceIntents.triggerForegroundService(
                                context = context,
                                action = if (currentState == StopwatchState.Started) Constants.ACTION_SERVICE_STOP else Constants.ACTION_SERVICE_START
                            )
                            isProgressBarActive = shouldReset
                            shouldReset = !shouldReset
                        }
                    )
                } else {
                    PlayButton {
                        isStarted = true
                        isProgressBarActive = true
                        StopWatchServiceIntents.triggerForegroundService(
                            context = context,
                            action = Constants.ACTION_SERVICE_START
                        )
                    }
                }
            }
        }
    }
}