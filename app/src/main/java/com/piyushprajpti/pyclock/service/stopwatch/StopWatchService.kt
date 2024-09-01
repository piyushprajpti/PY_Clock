package com.piyushprajpti.pyclock.service.stopwatch

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.piyushprajpti.pyclock.util.Constants
import com.piyushprajpti.pyclock.util.Constants.STOPWATCH_STATE
import com.piyushprajpti.pyclock.util.formatTime
import com.piyushprajpti.pyclock.util.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@SuppressLint("RestrictedApi")
@AndroidEntryPoint
class StopWatchService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopwatchBinder()

    private var duration: Duration = Duration.ZERO

    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentState = mutableStateOf(StopwatchState.Idle)
        private set


    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            StopwatchState.Started.name -> {
                setPauseButton()
                startForegroundService()
                startStopwatch { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }

            StopwatchState.Paused.name -> {
                stopStopwatch()
                setResumeButton()
            }

            StopwatchState.Cancelled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                Constants.ACTION_SERVICE_START -> {
                    setPauseButton()
                    startForegroundService()
                    startStopwatch { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }
                }

                Constants.ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    setResumeButton()
                }

                Constants.ACTION_SERVICE_CANCEL -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = StopwatchState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = StopwatchState.Paused
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        currentState.value = StopwatchState.Idle
        updateTimeUnits()
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(Constants.STOPWATCH_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(Constants.STOPWATCH_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StopWatchService.hours.value = hours.toInt().pad()
            this@StopWatchService.minutes.value = minutes.pad()
            this@StopWatchService.seconds.value = seconds.pad()
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            Constants.STOPWATCH_NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            ).build()
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.STOPWATCH_CHANNEL_ID,
                Constants.STOPWATCH_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setPauseButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Pause",
                StopWatchServiceIntents.pausePendingIntent(this)
            )
        )
        notificationManager.notify(Constants.STOPWATCH_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                StopWatchServiceIntents.resumePendingIntent(this)
            )
        )
        notificationManager.notify(Constants.STOPWATCH_NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopWatchService = this@StopWatchService
    }
}

enum class StopwatchState { Idle, Started, Paused, Cancelled }