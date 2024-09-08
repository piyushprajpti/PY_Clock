package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
class FutureSelectableDates : SelectableDates {
    private val now = LocalDate.now()
    private val dayStart = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStart
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }
}

fun convertMillisInRemainingTime(time: Long): String {
    val currentMillis = System.currentTimeMillis()
    val remainingMillis = time - currentMillis

    val months = remainingMillis / 2592000000
    val days = remainingMillis / 86400000
    val hours = (remainingMillis % 86400000) / 3600000
    val minutes = (remainingMillis % 3600000) / 60000

    return when {
        months > 0 -> "Alarm will ring in $months months, $days days, $hours hours and $minutes minutes"
        days > 0 -> "Alarm will ring in $days days, $hours hours and $minutes minutes"
        hours > 0 -> "Alarm will ring in $hours hours and $minutes minutes"
        minutes > 0 -> "Alarm will ring in $minutes minutes"
        else -> "Alarm will ring in less one minute!"
    }
}

fun getDateTime(millis: Long): Triple<Int, Int, Long> {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply {
        timeInMillis = millis
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val dateMillis = calendar.timeInMillis

    return Triple(hour, minute, dateMillis)
}

fun timeFormatter(hour: Int, minute: Int): Triple<String, String, String> {
    val period = if (hour < 12) "AM" else "PM"
    val adjustedHour = when {
        hour == 0 -> "12"
        hour < 10 -> "0$hour"
        hour > 19 -> "${hour - 12}"
        hour > 12 -> "0${hour - 12}"
        else -> "$hour"
    }
    val adjustedMinute = when {
        minute < 10 -> "0$minute"
        else -> "$minute"
    }
    return Triple(adjustedHour, adjustedMinute, period)
}

fun covertDateTimeToMillis(dateMillis: Long, hourMillis: Int, minuteMillis: Int): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, hourMillis)
        set(Calendar.MINUTE, minuteMillis)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.timeInMillis
}

fun convertDateTimeToString(millis: Long): Quadruple<String, String, String, String> {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply {
        timeInMillis = millis
    }

    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val minute = calendar.get(Calendar.MINUTE)

    val updatedTime = timeFormatter(hour, minute)

    val period = if (hour >= 12) "PM" else "AM"

    val date = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    }.format(calendar.time)

    return Quadruple(updatedTime.first, updatedTime.second, period, date)
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
