package com.piyushprajpti.pyclock.presentation.alarm_screen.util

data class AlarmData(
    val isOn: Boolean,
    val time: Triple<String, String, String>,
    val date: String,
)