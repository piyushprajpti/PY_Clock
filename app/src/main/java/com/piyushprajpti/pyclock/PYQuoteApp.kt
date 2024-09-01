package com.piyushprajpti.pyclock

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.piyushprajpti.pyclock.presentation.alarm_screen.EditAlarmScreen
import com.piyushprajpti.pyclock.presentation.main_feed.MainFeed
import com.piyushprajpti.pyclock.presentation.setting_screen.SettingScreen
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService

@Composable
fun PYQuoteApp(
    selectedTheme: Int,
    stopWatchService: StopWatchService
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainFeed.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = Screen.MainFeed.route) {
            MainFeed(
                selectedTheme = selectedTheme,
                stopWatchService = stopWatchService,
                onSettingClick = {
                    navController.navigate(Screen.SettingScreen.route)
                },
                onAlarmCardClick = {
                    navController.navigate(Screen.EditAlarmScreen.route)
                }
            )
        }

        composable(route = Screen.SettingScreen.route) {
            SettingScreen(
                selectedTheme = selectedTheme,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = Screen.EditAlarmScreen.route) {
            EditAlarmScreen(
                redirectToAlarmScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}