package com.piyushprajpti.pyclock.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.piyushprajpti.pyclock.R
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchServiceIntents
import com.piyushprajpti.pyclock.service.timer.TimerServiceIntents
import com.piyushprajpti.pyclock.util.Constants
import com.piyushprajpti.pyclock.util.Constants.STOPWATCH_CHANNEL_ID
import com.piyushprajpti.pyclock.util.Constants.TIMER_CHANNEL_ID_1
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    @Named("stopwatchNotificationBuilder")
    fun providesStopWatchNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, STOPWATCH_CHANNEL_ID)
            .setContentTitle("Stopwatch")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_stopwatch)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(0, "Pause", StopWatchServiceIntents.pausePendingIntent(context))
            .addAction(0, "Cancel", StopWatchServiceIntents.cancelPendingIntent(context))
            .setContentIntent(StopWatchServiceIntents.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    @Named("timerNotificationBuilder")
    fun providesTimerNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, TIMER_CHANNEL_ID_1)
            .setContentTitle("Timer")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_timer)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(0, "Pause", TimerServiceIntents.pausePendingIntent(context))
            .addAction(0, "Cancel", TimerServiceIntents.cancelPendingIntent(context))
            .setContentIntent(TimerServiceIntents.clickPendingIntent(context))
    }

    fun createAlarmNotificationChannel(@ApplicationContext context: Context) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            Constants.ALARM_CHANNEL_ID,
            Constants.ALARM_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(alarmSound, audioAttributes)
            shouldVibrate()
            enableVibration(true)
            enableLights(true)
            vibrationPattern = longArrayOf(0, 500, 500, 500)
            setBypassDnd(true)
            importance = NotificationManager.IMPORTANCE_HIGH
        }

        notificationManager.createNotificationChannel(channel)

    }
}