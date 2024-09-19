package com.piyushprajpti.pyclock.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.convertMillisInRemainingTime

fun formatTime(hours: String, minutes:String, seconds: String): String {
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

val TextUnit.fix
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp

fun toastRemainingTime(time: Long, context: Context) {
    val message = convertMillisInRemainingTime(time)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}