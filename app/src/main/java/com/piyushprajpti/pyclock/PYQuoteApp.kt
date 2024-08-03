package com.piyushprajpti.pyclock

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.piyushprajpti.pyclock.presentation.main_feed.MainFeed
import com.piyushprajpti.pyclock.presentation.setting_screen.SettingScreen

@Composable
fun PYQuoteApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainFeed.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
    ) {
        composable(route = Screen.MainFeed.route) {
            MainFeed(onSettingClick = {
                navController.navigate(Screen.SettingScreen.route)
            })
        }

        composable(route = Screen.SettingScreen.route) {
            SettingScreen(onBackClick = {
                navController.popBackStack()
            })
        }
    }
}