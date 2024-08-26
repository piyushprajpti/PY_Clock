package com.piyushprajpti.pyclock.presentation.timer_screen

import android.util.Log
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
import androidx.compose.runtime.mutableLongStateOf
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
import com.piyushprajpti.pyclock.ui.theme.ErrorRed
import com.piyushprajpti.pyclock.ui.theme.VioletBlue
import com.piyushprajpti.pyclock.util.ActionButton
import com.piyushprajpti.pyclock.util.CircularProgressCanvas
import com.piyushprajpti.pyclock.util.PlayButton
import kotlinx.coroutines.launch

@Composable
fun TimerScreen(isDarkTheme: Boolean) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isStarted by remember { mutableStateOf(false) }

    var isPaused by remember { mutableStateOf(false) }

    var hour by remember { mutableStateOf("00") }

    var minute by remember { mutableStateOf("00") }

    var second by remember { mutableStateOf("30") }

    val progress = remember { Animatable(1f) }

    var totalMillis by remember { mutableLongStateOf(0L) }
    var remainingTime by remember { mutableStateOf("") }

    if (isStarted) {
        LaunchedEffect(progress.value) {
            val remainingMillis = (progress.value * totalMillis).toLong()
            val hours = (remainingMillis / 3600000).toInt()
            val minutes = ((remainingMillis % 3600000) / 60000).toInt()
            val seconds = ((remainingMillis % 60000) / 1000).toInt()
            remainingTime = "%02d:%02d:%02d".format(hours, minutes, seconds)
        }
    }

    fun onPlayClick() {

        if (minute.toInt() >= 60) {
            Toast.makeText(context, "Minute input range is 0 to 59", Toast.LENGTH_SHORT).show()
        } else if (second.toInt() >= 60) {
            Toast.makeText(context, "Second input range is 0 to 59", Toast.LENGTH_SHORT).show()
        } else {
            isStarted = true
            totalMillis =
                (hour.toInt() * 3600 + minute.toInt() * 60 + second.toInt()).toLong() * 1000L
            Log.d(
                "TAG",
                "onPlayClick: ${hour.toInt()}, ${minute.toInt()}, ${second.toInt()}, $totalMillis"
            )

            coroutineScope.launch {
                progress.snapTo(1f)
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = totalMillis.toInt(),
                        easing = LinearEasing
                    )
                )
                isStarted = false
            }
        }
    }

    fun onPauseClick() {

        if (isPaused) {
            val remainingMillis = (progress.value * totalMillis).toInt()
            coroutineScope.launch {
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = remainingMillis, easing = LinearEasing)
                )
                isStarted = false
            }
        } else {
            coroutineScope.launch {
                progress.stop()
            }
        }
        isPaused = !isPaused
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
                        text = remainingTime,
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
                    .weight(1f)
            ) {

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberInputField(
                        label = "HH",
                        value = hour,
                        onValueChange = { hour = it }
                    )

                    NumberInputField(
                        label = "MM",
                        value = minute,
                        onValueChange = { minute = it }
                    )

                    NumberInputField(
                        label = "SS",
                        value = second,
                        onValueChange = { second = it }
                    )
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
                    onClick = {
                        isStarted = false
                        isPaused = false
                    }
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