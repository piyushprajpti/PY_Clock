package com.piyushprajpti.pyclock.presentation

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun setTheme(key: String, value: Int) {
        viewModelScope.launch {
            dataStoreRepository.setValue(intPreferencesKey(key), value)
        }
    }

    private fun getTheme(key: String): Flow<Int?> {
        return dataStoreRepository.getValue(intPreferencesKey(key))
    }

    suspend fun isAppInDarkTheme(): Int? {
        val theme = getTheme("selected_theme")
        return theme.firstOrNull()
    }
}