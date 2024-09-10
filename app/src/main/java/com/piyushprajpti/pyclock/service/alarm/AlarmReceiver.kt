package com.piyushprajpti.pyclock.service.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.piyushprajpti.pyclock.R
import com.piyushprajpti.pyclock.di.CommonModule
import com.piyushprajpti.pyclock.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var db: CommonModule.PYClockDatabase

    override fun onReceive(context: Context?, intent: Intent?) {

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (intent?.action == Constants.ACTION_DISMISS_ALARM) {
            vibrator.cancel()
            return
        }

        val alarmId = intent?.getIntExtra(Constants.ALARM_ID, -1)
        if (alarmId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                db.alarmDao().updateAlarmStatus(alarmId, false)
            }
        }

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setStreamVolume(
                AudioManager.STREAM_ALARM,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                0
            )
        }
        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 1000, 1000), 0))

        val notification =
            NotificationCompat.Builder(context.applicationContext, Constants.ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Alarm is ringing!")
                .setContentText("Swipe to dismiss")
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDeleteIntent(AlarmServiceIntents.dismissIntent(context))
                .build()

        notificationManager.notify(Constants.ALARM_NOTIFICATION_ID, notification)
    }
}