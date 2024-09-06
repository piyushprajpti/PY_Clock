package com.piyushprajpti.pyclock.service.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.piyushprajpti.pyclock.R
import com.piyushprajpti.pyclock.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val notification =
                NotificationCompat.Builder(context.applicationContext, Constants.ALARM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.share)
                    .setContentTitle("Alarm is ringing!")
                    .setContentText("Swipe to dismiss")
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVibrate(longArrayOf(0, 1000))
                    .setAutoCancel(true)
                    .build()

            notificationManager.notify(Constants.ALARM_NOTIFICATION_ID, notification)
        }
    }
}