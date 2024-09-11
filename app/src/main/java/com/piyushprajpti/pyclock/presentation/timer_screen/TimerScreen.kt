package com.piyushprajpti.pyclock.presentation.timer_screen

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.service.timer.TimerService
import com.piyushprajpti.pyclock.service.timer.TimerServiceIntents
import com.piyushprajpti.pyclock.service.timer.TimerState
import com.piyushprajpti.pyclock.ui.theme.ErrorRed
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.ActionButton
import com.piyushprajpti.pyclock.util.CircularProgressCanvas
import com.piyushprajpti.pyclock.util.Constants
import com.piyushprajpti.pyclock.util.PlayButton
import kotlinx.coroutines.launch

@Composable
fun TimerScreen(
    isDarkTheme: Boolean,
    timerService: TimerService,
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val totalMillis by timerService.totalMillis
    val currentState by timerService.currentState

    var isStarted by remember {
        mutableStateOf(
            currentState == TimerState.Started || currentState == TimerState.Paused
        )
    }
    var isPaused by remember { mutableStateOf(currentState == TimerState.Paused) }

    var inputHours by remember { mutableStateOf("00") }
    var inputMinutes by remember { mutableStateOf("00") }
    var inputSeconds by remember { mutableStateOf("30") }

    var isProgressBarActive by remember { mutableStateOf(currentState == TimerState.Started) }
    val progress = remember { Animatable(1f) }
    var snapToProgress by remember { mutableStateOf(progress.value) }

    LaunchedEffect(Unit) {
        if (currentState == TimerState.Started || currentState == TimerState.Paused) {
            val temp =
                hours.toLong() * 360000L + minutes.toLong() * 60000L + seconds.toLong() * 1000L

            snapToProgress = temp.toFloat() / totalMillis
            progress.snapTo(snapToProgress)

        }
    }

    LaunchedEffect(isProgressBarActive) {
        if (isProgressBarActive) {

            val remainingMillis =
                hours.toLong() * 3600000L + minutes.toLong() * 60000L + seconds.toLong() * 1000L

            coroutineScope.launch {
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = remainingMillis.toInt(),
                        easing = LinearEasing
                    )
                )
            }

        } else {
            coroutineScope.launch {
                progress.stop()
            }
        }
    }

    LaunchedEffect(currentState) {
        isStarted = currentState != TimerState.Idle
        isPaused = currentState == TimerState.Paused
        isProgressBarActive = currentState == TimerState.Started
    }

    fun onPlayClick() {

        val isInvalidInput =
            inputHours.any { it.isDigit() } || inputMinutes.any { it.isDigit() } || inputSeconds.any { it.isDigit() }

        if (!isInvalidInput) {
            Toast.makeText(
                context,
                "Please enter valid numeric values for hours, minutes, and seconds",
                Toast.LENGTH_SHORT
            ).show()
        } else if (inputMinutes.toInt() >= 60) {
            Toast.makeText(context, "Minute input range is 0 to 59", Toast.LENGTH_SHORT).show()
        } else if (inputSeconds.toInt() >= 60) {
            Toast.makeText(context, "Second input range is 0 to 59", Toast.LENGTH_SHORT).show()
        } else {
            isStarted = true
            coroutineScope.launch {
                progress.snapTo(1f)
            }
            TimerServiceIntents.triggerForegroundService(
                context = context,
                action = Constants.ACTION_SERVICE_START,
                hours = inputHours,
                minutes = inputMinutes,
                seconds = inputSeconds
            )

        }
    }

    fun onPauseClick() {

        TimerServiceIntents.triggerForegroundService(
            context = context,
            action = if (currentState == TimerState.Started) Constants.ACTION_SERVICE_STOP else Constants.ACTION_SERVICE_START
        )
        isPaused = !isPaused
    }

    fun onDeleteClick() {
        isStarted = false
        isPaused = false
        TimerServiceIntents.triggerForegroundService(
            context = context,
            action = Constants.ACTION_SERVICE_CANCEL
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isStarted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                        modifier = Modifier.fillMaxSize(),
                        progress = progress.value
                    )

                    Text(
                        text = "$hours:$minutes:$seconds",
                        fontSize = 32.sp,
                        letterSpacing = 3.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NumberInputField(
                            label = "HH",
                            value = inputHours,
                            onValueChange = { inputHours = it }
                        )

                        NumberInputField(
                            label = "MM",
                            value = inputMinutes,
                            onValueChange = { inputMinutes = it }
                        )

                        NumberInputField(
                            label = "SS",
                            value = inputSeconds,
                            onValueChange = { inputSeconds = it }
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimeBubble(time = "00:02:00") {
                            inputMinutes = "02"
                            inputSeconds = "00"
                            onPlayClick()
                        }
                        TimeBubble(time = "00:05:00") {
                            inputMinutes = "05"
                            inputSeconds = "00"
                            onPlayClick()
                        }
                        TimeBubble(time = "00:10:00") {
                            inputMinutes = "10"
                            inputSeconds = "00"
                            onPlayClick()
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))
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
                    title = "Delete",
                    titleColor = MaterialTheme.colorScheme.primary,
                    backColor = MaterialTheme.colorScheme.secondary,
                    onClick = { onDeleteClick() }
                )

                ActionButton(
                    title = if (isPaused) "Resume" else "Pause",
                    titleColor = Color.White,


                    backColor = if (isPaused) VioletBlue else ErrorRed,
                    onClick = { onPauseClick() }
                )
            } else {
                PlayButton(onClick = { onPlayClick() })
            }
        }
    }
}