package com.piyushprajpti.pyclock.domain.repository

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.piyushprajpti.pyclock.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val notificationManager: NotificationManager
): ViewModel() {
    fun sendTimerNotification(time: String, context: Context) {
        val notification = NotificationCompat.Builder(context, "id")
            .setSmallIcon(R.drawable.notification_logo)
            .setContentTitle("Timer Ended")
            .setContentText(time)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .build()
        notificationManager.notify(1, notification)
    }
}