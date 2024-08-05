package com.piyushprajpti.pyclock

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import com.piyushprajpti.pyclock.ui.theme.PY_ClockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preference")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val configuration = LocalConfiguration.current
            val uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

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
                else -> (uiMode == Configuration.UI_MODE_NIGHT_YES)
            }

            PY_ClockTheme(darkTheme = isDarkTheme) {
                PYQuoteApp(selectedTheme.value)
            }
        }
    }
}