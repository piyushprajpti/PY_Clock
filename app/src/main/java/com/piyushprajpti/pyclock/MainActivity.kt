package com.piyushprajpti.pyclock

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.service.timer.TimerService
import com.piyushprajpti.pyclock.ui.theme.PY_ClockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isStopwatchBound by mutableStateOf(false)
    private var isTimerBound by mutableStateOf(false)

    private lateinit var stopWatchService: StopWatchService
    private lateinit var timerService: TimerService

    private val stopwatchConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StopWatchService.StopwatchBinder
            stopWatchService = binder.getService()
            isStopwatchBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isStopwatchBound = false
        }
    }

    private val timerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isTimerBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isTimerBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopWatchService::class.java).also { intent ->
            bindService(intent, stopwatchConnection, Context.BIND_AUTO_CREATE)
        }

        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, timerConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !isTimerBound
                !isStopwatchBound
            }
        }

        setContent {

            // app theme logics
            val configuration = LocalConfiguration.current

            val dataStoreRepository = DataStoreRepository(applicationContext.dataStore)
            val initialThemeValue = runBlocking {
                dataStoreRepository.getValue(intPreferencesKey("selected_theme")).firstOrNull() ?: 0
            }

            val themePref =
                dataStoreRepository.getValue(intPreferencesKey("selected_theme")).collectAsState(
                    initial = initialThemeValue
                )

            val selectedTheme = remember {
                derivedStateOf { themePref.value ?: 0 }
            }

            val isSystemInDarkTheme =
                (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES


            val isDarkTheme = when (selectedTheme.value) {
                2 -> false
                3 -> true
                else -> isSystemInDarkTheme
            }

            PY_ClockTheme(darkTheme = isDarkTheme) {
                if (isStopwatchBound && isTimerBound) {
                    PYQuoteApp(
                        selectedTheme = selectedTheme.value,
                        stopWatchService = stopWatchService,
                        timerService = timerService
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(stopwatchConnection)
        isStopwatchBound = false
    }
}