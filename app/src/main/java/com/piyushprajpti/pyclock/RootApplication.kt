package com.piyushprajpti.pyclock

import android.app.Application
import com.piyushprajpti.pyclock.di.NotificationModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RootApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationModule.createAlarmNotificationChannel(this)
    }
}