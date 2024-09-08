package com.piyushprajpti.pyclock

sealed class Screen(val route: String) {
    data object MainFeed : Screen("main_feed")
    data object SettingScreen : Screen("setting_screen")
    data object EditAlarmScreen : Screen("edit_alarm_screen") {
        fun setId(alarmId: String?) = "edit_alarm_screen?alarmId=$alarmId"
        fun getId() = "edit_alarm_screen?alarmId={alarmId}"
    }
}