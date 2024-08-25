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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.ErrorRed
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.ActionButton
import com.piyushprajpti.pyclock.util.CircularProgressCanvas
import com.piyushprajpti.pyclock.util.PlayButton
import kotlinx.coroutines.delay

@Composable
fun StopWatchScreen(
    isDarkTheme: Boolean
) {

    var isStarted by remember { mutableStateOf(false) }

    var shouldReset by remember { mutableStateOf(false) }

    var elapsedTimeText by remember { mutableStateOf("00:00:00") }

    var isProgressBarActive by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }

    val durationMillis = 60 * 1000L

    var startTime by remember { mutableStateOf(0L) }
    var elapsedMillis by remember { mutableStateOf(0L) }
    var totalElapsedMillis by remember { mutableStateOf(0L) }


    LaunchedEffect(isProgressBarActive) {
        if (isProgressBarActive) {
            startTime = System.currentTimeMillis()
        }
        while (isProgressBarActive) {
            val currentTime = System.currentTimeMillis()
            val newElapsedMillis = elapsedMillis + (currentTime - startTime)
            startTime = currentTime

            if (newElapsedMillis >= durationMillis) {
                elapsedMillis = 0L
                progress = 0f
                totalElapsedMillis += durationMillis
            } else {
                elapsedMillis = newElapsedMillis
                progress = (newElapsedMillis.toFloat() / durationMillis).coerceIn(0f, 1f)
            }

            elapsedTimeText =
                formatElapsedTime(totalElapsedMillis + elapsedMillis)
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

    var lapCount by remember {
        mutableIntStateOf(1)
    }

    var previousLapTime by remember {
        mutableLongStateOf(0L)
    }

    fun onResetClick() {
        progress = 0f
        isStarted = false
        shouldReset = false
        elapsedMillis = 0L
        totalElapsedMillis = 0L
        elapsedTimeText = "00:00:00"
        lapsList.value = listOf()
        lapCount = 1
        previousLapTime = 0L
    }

    fun onLapClick() {
        val newTotalTime = totalElapsedMillis + elapsedMillis
        val newLapTime = if (previousLapTime == 0L) {
            newTotalTime
        } else {
            newTotalTime - previousLapTime
        }
        previousLapTime = newTotalTime
        val newLap = LapData(
            lapCount,
            formatElapsedTime(newLapTime),
            formatElapsedTime(newTotalTime)
        )
        lapsList.value += newLap
        lapCount += 1
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
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier
                        .fillMaxSize(),
                    progress = progress
                )

                Text(
                    text = elapsedTimeText,
                    fontSize = 32.sp,
                    letterSpacing = 3.sp,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (lapCount > 1) {
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
                        isProgressBarActive = shouldReset
                        shouldReset = !shouldReset
                    }
                )
            } else {
                PlayButton {
                    isStarted = true
                    isProgressBarActive = true
                }
            }
        }

    }
}