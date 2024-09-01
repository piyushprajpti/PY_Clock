package com.piyushprajpti.pyclock.service.timer

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
import com.piyushprajpti.pyclock.util.formatTime
import com.piyushprajpti.pyclock.util.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
@SuppressLint("RestrictedApi")
class TimerService : Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    @Named("timerNotificationBuilder")
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = TimerBinder()

    private var duration: Duration = Duration.ZERO

    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentState = mutableStateOf(TimerState.Idle)
        private set

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.getStringExtra(Constants.HOURS) != null) {
            hours.value = intent.getStringExtra(Constants.HOURS)!!
            minutes.value = intent.getStringExtra(Constants.MINUTES)!!
            seconds.value = intent.getStringExtra(Constants.SECONDS)!!

            val hoursDuration = hours.value.toLong().hours
            val minutesDuration = minutes.value.toLong().minutes
            val secondsDuration = seconds.value.toLong().seconds

            duration = duration.plus(hoursDuration + minutesDuration + secondsDuration)
        }

        when (intent?.getStringExtra(Constants.TIMER_STATE)) {
            TimerState.Started.name -> {
                setPauseButton()
                startForegroundService()
                startTimer { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }

            TimerState.Paused.name -> {
                stopTimer()
                setResumeButton()
            }

            TimerState.Cancelled.name -> {
                stopTimer()
                cancelTimer()
                stopForegroundService()
            }
        }

        intent?.action.let {
            when (it) {
                Constants.ACTION_SERVICE_START -> {
                    setPauseButton()
                    startForegroundService()
                    startTimer { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }
                }

                Constants.ACTION_SERVICE_STOP -> {
                    stopTimer()
                    setResumeButton()
                }

                Constants.ACTION_SERVICE_CANCEL -> {
                    stopTimer()
                    cancelTimer()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = TimerState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.minus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = TimerState.Paused
    }

    private fun cancelTimer() {
        duration = Duration.ZERO
        currentState.value = TimerState.Idle
        updateTimeUnits()
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(Constants.TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(Constants.TIMER_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@TimerService.hours.value = hours.toInt().pad()
            this@TimerService.minutes.value = minutes.pad()
            this@TimerService.seconds.value = seconds.pad()
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            Constants.TIMER_NOTIFICATION_ID,
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
                Constants.TIMER_CHANNEL_ID,
                Constants.TIMER_CHANNEL_NAME,
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
                TimerServiceIntents.pausePendingIntent(this)
            )
        )
        notificationManager.notify(Constants.TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                TimerServiceIntents.resumePendingIntent(this)
            )
        )
        notificationManager.notify(Constants.TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

}

enum class TimerState { Idle, Started, Paused, Cancelled }