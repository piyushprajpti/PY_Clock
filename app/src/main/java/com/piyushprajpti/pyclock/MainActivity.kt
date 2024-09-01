package com.piyushprajpti.pyclock

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchService
import com.piyushprajpti.pyclock.ui.theme.PY_ClockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var stopWatchService: StopWatchService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StopWatchService.StopwatchBinder
            stopWatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopWatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // app theme logics
            val configuration = LocalConfiguration.current

            val dataStoreRepository = DataStoreRepository(applicationContext.dataStore)
            val initialThemeValue = runBlocking {
                dataStoreRepository.getValue(intPreferencesKey("selected_theme")).firstOrNull() ?: 1
            }

            val themePref =
                dataStoreRepository.getValue(intPreferencesKey("selected_theme")).collectAsState(
                    initial = initialThemeValue
                )

            val selectedTheme = remember {
                derivedStateOf { themePref.value ?: 1 }
            }

            val isDarkTheme = when (selectedTheme.value) {
                2 -> false
                3 -> true
                else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    configuration.isNightModeActive
                } else {
                    if (configuration.uiMode == 33) true else false
                }
            }

            // notification logics
            val context = LocalContext.current

            var hasNotificationPermission by remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                } else mutableStateOf(true)
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                }
            )

            SideEffect {
                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            PY_ClockTheme(darkTheme = isDarkTheme) {
                if (isBound) {
                    PYQuoteApp(
                        selectedTheme = selectedTheme.value,
                        stopWatchService = stopWatchService
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}