package com.piyushprajpti.pyclock.presentation.alarm_screen.util

import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.ViewModel
import com.piyushprajpti.pyclock.service.alarm.AlarmServiceIntents
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmManager: AlarmManager,

    @ApplicationContext
    private val context: Context
) : ViewModel() {

    fun scheduleAlarm(time: Long) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmServiceIntents.scheduleIntent(context, time)
        )
    }

    fun deleteAlarm(time: Long) {
        alarmManager.cancel(AlarmServiceIntents.cancelIntent(context, time))
    }
}