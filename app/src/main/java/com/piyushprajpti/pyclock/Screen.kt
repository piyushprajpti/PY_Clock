package com.piyushprajpti.pyclock

sealed class Screen(val route: String) {
    data object MainFeed: Screen("main_feed")
    data object SettingScreen: Screen("setting_screen")
}