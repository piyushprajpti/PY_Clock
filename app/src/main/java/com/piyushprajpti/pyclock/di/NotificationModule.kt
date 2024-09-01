package com.piyushprajpti.pyclock.di

import android.content.Context
import androidx.core.app.NotificationCompat
import com.piyushprajpti.pyclock.R
import com.piyushprajpti.pyclock.service.stopwatch.StopWatchServiceIntents
import com.piyushprajpti.pyclock.util.Constants.STOPWATCH_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
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
}