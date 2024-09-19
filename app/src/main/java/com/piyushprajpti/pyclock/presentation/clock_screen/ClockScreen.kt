package com.piyushprajpti.pyclock.presentation.clock_screen

import android.content.res.Configuration
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.ClockPrimaryDT
import com.piyushprajpti.pyclock.ui.theme.ClockPrimaryLT
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryDT
import com.piyushprajpti.pyclock.ui.theme.ClockSecondaryLT
import kotlinx.coroutines.delay
import java.util.Date

@Composable
fun ClockScreen(
    isDarkTheme: Boolean,
) {
    val configuration = LocalConfiguration.current

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

    val _dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val date = calendar.get(Calendar.DATE)
    val _month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    val dayOfTheWeek = when (_dayOfTheWeek) {
        1 -> "Sunday"
        2 -> "Monday"
        3 -> "Tuesday"
        4 -> "Wednesday"
        5 -> "Thursday"
        6 -> "Friday"
        7 -> "Saturday"
        else -> "DayOfTheWeek"
    }

    val month = when (_month) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "September"
        9 -> "October"
        10 -> "November"
        11 -> "December"
        else -> "Month"
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

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClockCanvas(
                second = second.value,
                minute = minute.value,
                hour = hour.value,
                outerColor = if (isDarkTheme) ClockPrimaryDT else ClockPrimaryLT,
                innerColor = if (isDarkTheme) ClockSecondaryDT else ClockSecondaryLT,
                primaryColor = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
            )

            Text(
                text = "$dayOfTheWeek, $date $month $year",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(5.dp))

            ClockCanvas(
                second = second.value,
                minute = minute.value,
                hour = hour.value,
                outerColor = if (isDarkTheme) ClockPrimaryDT else ClockPrimaryLT,
                innerColor = if (isDarkTheme) ClockSecondaryDT else ClockSecondaryLT,
                primaryColor = if (isDarkTheme) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "$dayOfTheWeek, $date $month $year",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}