package com.piyushprajpti.pyclock.presentation

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    fun setValueInPref(key: String, value: String) {
        viewModelScope.launch {
            dataStoreRepository.setValue(stringPreferencesKey(key), value)
        }
    }

    fun setTheme(key: String, value: Int) {
        viewModelScope.launch {
            dataStoreRepository.setValue(intPreferencesKey(key), value)
        }
    }

    suspend fun getValueFromPref(key: String): String? {
        return dataStoreRepository.getValue(stringPreferencesKey(key)).firstOrNull()

    }
}