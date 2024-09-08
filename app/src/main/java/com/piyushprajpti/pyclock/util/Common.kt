package com.piyushprajpti.pyclock.util

import android.content.Context
import android.widget.Toast
import com.piyushprajpti.pyclock.presentation.alarm_screen.util.convertMillisInRemainingTime

fun formatTime(hours: String, minutes:String, seconds: String): String {
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun toastRemainingTime(time: Long, context: Context) {
    val message = convertMillisInRemainingTime(time)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}