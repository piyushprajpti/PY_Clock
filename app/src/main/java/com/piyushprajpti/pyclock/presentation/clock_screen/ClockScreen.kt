package com.piyushprajpti.pyclock.presentation.clock_screen

import android.icu.util.Calendar
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.presentation.CommonViewModel
import com.piyushprajpti.pyclock.ui.theme.ClockPrimaryDT
import com.piyushprajpti.pyclock.ui.theme.ClockPrimaryLT
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryDT
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryLT
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun ClockScreen(
    commonViewModel: CommonViewModel = hiltViewModel()
) {

    val calendar = Calendar.getInstance()
    val second = remember {
        mutableStateOf(calendar.get(Calendar.SECOND))
    }
    val minute = remember {
        mutableStateOf(calendar.get(Calendar.MINUTE))
    }
    val hour = remember {
        mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            calendar.time = Date()
            second.value = calendar.get(Calendar.SECOND)
            minute.value = calendar.get(Calendar.MINUTE)
            hour.value = calendar.get(Calendar.HOUR_OF_DAY)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ClockCanvas(
                second = second.value,
                minute = minute.value,
                hour = hour.value,
                outerColor = if (isSystemInDarkTheme()) ClockPrimaryDT else ClockPrimaryLT,
                innerColor = if (isSystemInDarkTheme()) ClockSecondaryDT else ClockSecondaryLT,
                primaryColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .fillParentMaxHeight(0.4f)
            )
        }
    }
}