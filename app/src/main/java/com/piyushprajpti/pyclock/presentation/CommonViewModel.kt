package com.piyushprajpti.pyclock.presentation

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyushprajpti.pyclock.data.local_storage.stopwatch.LapData
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import com.piyushprajpti.pyclock.di.CommonModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val db: CommonModule.PYClockDatabase
) : ViewModel() {

    fun setTheme(key: String, value: Int) {
        viewModelScope.launch {
            dataStoreRepository.setValue(intPreferencesKey(key), value)
        }
    }

    private fun getTheme(key: String): Flow<Int?> {
        return dataStoreRepository.getValue(intPreferencesKey(key))
    }

    suspend fun getLapList(): List<LapData> {
        return withContext(Dispatchers.IO) {
            db.lapDao().getAllLaps()
        }
    }

    suspend fun addLap(lapList: List<LapData>) {
        withContext(Dispatchers.IO) {
            db.lapDao().insertLap(lapList)
        }
    }

    suspend fun clearLapData() {
        withContext(Dispatchers.IO) {
            db.lapDao().clearLapData()
        }
    }
}