package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyushprajpti.pyclock.data.local_storage.alarm.AlarmData
import com.piyushprajpti.pyclock.di.CommonModule
import com.piyushprajpti.pyclock.service.alarm.AlarmServiceIntents
import com.piyushprajpti.pyclock.util.Constants
import com.piyushprajpti.pyclock.util.toastRemainingTime
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmManager: AlarmManager,
    private val db: CommonModule.PYClockDatabase,

    @ApplicationContext
    private val context: Context
) : ViewModel() {

    fun scheduleAlarm(alarmData: AlarmData) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmData.time,
            AlarmServiceIntents.scheduleIntent(context, alarmData.id)
        )
        toastRemainingTime(alarmData.time, context)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.alarmDao().upsertAlarm(alarmData)
            }
        }
    }

    fun deleteAlarm(alarmId: Int) {
        viewModelScope.launch {
            alarmManager.cancel(AlarmServiceIntents.cancelIntent(context, alarmId))
            withContext(Dispatchers.IO) {
                db.alarmDao().deleteAlarm(alarmId)
            }
        }
    }

    suspend fun getAlarmDataById(alarmId: Int): AlarmData {
        return withContext(Dispatchers.IO) {
            db.alarmDao().getAlarmById(alarmId)
        }

    }

    fun updateAlarmStatus(alarmData: AlarmData) {
        val currentMillis = System.currentTimeMillis()
        if (currentMillis >= alarmData.time) {
            alarmData.time += Constants.TOMORROW_MILLIS
        }

        viewModelScope.launch {
            if (alarmData.isOn) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmData.time,
                    AlarmServiceIntents.scheduleIntent(context, alarmData.id)
                )
                toastRemainingTime(alarmData.time, context)
            } else {
                alarmManager.cancel(AlarmServiceIntents.cancelIntent(context, alarmData.id))
            }
            withContext(Dispatchers.IO) {
                db.alarmDao().upsertAlarm(alarmData)
            }
        }
    }

    fun updateAlarmAfterRing(alarmId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
            }
        }
    }

    suspend fun getAllAlarms(): List<AlarmData> {
        return withContext(Dispatchers.IO) {
            db.alarmDao().getAllAlarms()
        }
    }
}