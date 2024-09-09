package com.piyushprajpti.pyclock.service.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.piyushprajpti.pyclock.util.Constants

object AlarmServiceIntents {

    private val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    fun scheduleIntent(context: Context, alarmId: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(Constants.ALARM_ID, alarmId)
        }
        return PendingIntent.getBroadcast(context, alarmId, intent, flag)
    }

    fun cancelIntent(context: Context, alarmId: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(context, alarmId, intent, flag)
    }

    fun dismissIntent(context: Context): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.ACTION_DISMISS_ALARM
        }
        return PendingIntent.getBroadcast(context, 0, intent, flag)
    }

//    fun fullScreenIntent(context: Context, time: String, message: String): PendingIntent {
//        val intent = Intent(context, AlarmActivity::class.java).apply {
//            putExtra("time", time)
//            putExtra("message", message)
//        }
//        return PendingIntent.getActivity(context, time.hashCode(), intent, flag)
//    }
}