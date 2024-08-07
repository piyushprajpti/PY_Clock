package com.piyushprajpti.pyclock.presentation.stopwatch_screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryDT
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryLT
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.ActionButton
import com.piyushprajpti.pyclock.util.CircularProgressCanvas
import com.piyushprajpti.pyclock.util.PlayButton
import kotlinx.coroutines.delay

@Composable
fun StopWatchScreen(
    isDarkTheme: Boolean
) {

    val isStarted = remember { mutableStateOf(false) }

    val shouldReset = remember { mutableStateOf(false) }

    val elapsedTimeText = remember { mutableStateOf("00:00:00") }

    val isProgressBarActive = remember { mutableStateOf(false) }

    val progress = remember { mutableFloatStateOf(0f) }

    val durationMillis = 60 * 1000L

    val startTime = remember { mutableStateOf(0L) }
    val elapsedMillis = remember { mutableStateOf(0L) }
    val totalElapsedMillis = remember { mutableStateOf(0L) }


    LaunchedEffect(isProgressBarActive.value) {
        if (isProgressBarActive.value) {
            startTime.value = System.currentTimeMillis()
        }
        while (isProgressBarActive.value) {
            val currentTime = System.currentTimeMillis()
            val newElapsedMillis = elapsedMillis.value + (currentTime - startTime.value)
            startTime.value = currentTime

            if (newElapsedMillis >= durationMillis) {
                elapsedMillis.value = 0L
                progress.floatValue = 0f
                totalElapsedMillis.value += durationMillis
            } else {
                elapsedMillis.value = newElapsedMillis
                progress.floatValue = (newElapsedMillis.toFloat() / durationMillis).coerceIn(0f, 1f)
            }

            elapsedTimeText.value =
                formatElapsedTime(totalElapsedMillis.value + elapsedMillis.value)
            delay(33L)

        }
    }

    data class LapData(
        val lapCount: Int,
        val lapTime: String,
        val totalTime: String
    )

    val lapsList = remember {
        mutableStateOf(listOf<LapData>())
    }

    val lapCount = remember {
        mutableIntStateOf(1)
    }

    val previousLapTime = remember {
        mutableLongStateOf(0L)
    }

    fun onResetClick() {
        progress.floatValue = 0f
        isStarted.value = false
        shouldReset.value = false
        elapsedMillis.value = 0L
        totalElapsedMillis.value = 0L
        elapsedTimeText.value = "00:00:00"
        lapsList.value = listOf()
        lapCount.intValue = 1
        previousLapTime.longValue = 0L
    }

    fun onLapClick() {
        val newTotalTime = totalElapsedMillis.value + elapsedMillis.value
        val newLapTime = if (previousLapTime.longValue == 0L) {
            newTotalTime
        } else {
            newTotalTime - previousLapTime.longValue
        }
        previousLapTime.longValue = newTotalTime
        val newLap = LapData(
            lapCount.intValue,
            formatElapsedTime(newLapTime),
            formatElapsedTime(newTotalTime)
        )
        lapsList.value += newLap
        lapCount.intValue += 1
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressCanvas(
                    modifier = Modifier
                        .fillMaxSize(),
                    circumferenceColor = if (isDarkTheme) ClockSecondaryDT else ClockSecondaryLT,
                    centerColor = MaterialTheme.colorScheme.background,
                    progress = progress.floatValue
                )

                Text(
                    text = elapsedTimeText.value,
                    fontSize = 32.sp,
                    letterSpacing = 3.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (lapCount.intValue > 1) {
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
            if (isStarted.value) {
                ActionButton(
                    title = if (shouldReset.value) "Reset" else "Lap",
                    titleColor = MaterialTheme.colorScheme.primary,
                    backColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        if (shouldReset.value) {
                            onResetClick()
                        } else {
                            onLapClick()
                        }
                    }
                )

                ActionButton(
                    title = if (shouldReset.value) "Resume" else "Pause",
                    titleColor = Color.White,
                    backColor = if (shouldReset.value) VioletBlue else Color.Red,
                    onClick = {
                        isProgressBarActive.value = shouldReset.value
                        shouldReset.value = !shouldReset.value
                    }
                )
            } else {
                PlayButton {
                    isStarted.value = true
                    isProgressBarActive.value = true
                }
            }
        }

    }
}