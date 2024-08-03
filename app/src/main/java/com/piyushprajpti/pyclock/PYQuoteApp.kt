package com.piyushprajpti.pyclock

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.piyushprajpti.pyclock.presentation.main_feed.MainFeed
import com.piyushprajpti.pyclock.presentation.setting_screen.SettingScreen

@Composable
fun PYQuoteApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainFeed.route) {
        composable(route = Screen.MainFeed.route) {
            MainFeed()
        }

        composable(route = Screen.SettingScreen.route) {
            SettingScreen()
        }
    }
}