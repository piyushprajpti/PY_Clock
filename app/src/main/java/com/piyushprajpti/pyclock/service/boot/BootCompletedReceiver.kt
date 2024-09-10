package com.piyushprajpti.pyclock.service.boot

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.piyushprajpti.pyclock.di.CommonModule
import com.piyushprajpti.pyclock.service.alarm.AlarmServiceIntents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var db: CommonModule.PYClockDatabase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (context != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val alarmList = db.alarmDao().getAllScheduledAlarms()

                    if (alarmList.isNotEmpty()) {
                        alarmList.forEach { alarmData ->
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                alarmData.time,
                                AlarmServiceIntents.scheduleIntent(context, alarmData.id)
                            )
                        }
                    }
                }
            }
        }
    }
}