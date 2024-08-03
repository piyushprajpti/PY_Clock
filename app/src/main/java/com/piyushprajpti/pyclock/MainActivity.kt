package com.piyushprajpti.pyclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.piyushprajpti.pyclock.presentation.main_feed.MainFeed
import com.piyushprajpti.pyclock.ui.theme.PY_ClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PY_ClockTheme {
                PYQuoteApp()
            }
        }
    }
}