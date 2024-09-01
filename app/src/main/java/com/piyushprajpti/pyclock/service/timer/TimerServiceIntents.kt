package com.piyushprajpti.pyclock.service.timer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.piyushprajpti.pyclock.MainActivity
import com.piyushprajpti.pyclock.util.Constants.CANCEL_REQUEST_CODE
import com.piyushprajpti.pyclock.util.Constants.CLICK_REQUEST_CODE
import com.piyushprajpti.pyclock.util.Constants.HOURS
import com.piyushprajpti.pyclock.util.Constants.MINUTES
import com.piyushprajpti.pyclock.util.Constants.RESUME_REQUEST_CODE
import com.piyushprajpti.pyclock.util.Constants.SECONDS
import com.piyushprajpti.pyclock.util.Constants.STOP_REQUEST_CODE
import com.piyushprajpti.pyclock.util.Constants.TIMER_STATE

object TimerServiceIntents {
    private val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PendingIntent.FLAG_IMMUTABLE
    } else 0

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, clickIntent, flag)
    }

    fun pausePendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Paused.name)
        }
        return PendingIntent.getService(context, STOP_REQUEST_CODE, stopIntent, flag)
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getService(context, RESUME_REQUEST_CODE, resumeIntent, flag)
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Cancelled.name)
        }
        return PendingIntent.getService(context, CANCEL_REQUEST_CODE, cancelIntent, flag)
    }

    fun triggerForegroundService(
        context: Context,
        action: String,
        hours: String? = null,
        minutes: String? = null,
        seconds: String? = null
    ) {
        Intent(context, TimerService::class.java).apply {
            this.action = action
            if (hours != null && minutes != null && seconds != null) {
                putExtra(HOURS, hours)
                putExtra(MINUTES, minutes)
                putExtra(SECONDS, seconds)
            }
            context.startService(this)
        }
    }
}