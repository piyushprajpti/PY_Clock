package com.piyushprajpti.pyclock.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.piyushprajpti.pyclock.data.local_storage.alarm.AlarmDao
import com.piyushprajpti.pyclock.data.local_storage.alarm.AlarmData
import com.piyushprajpti.pyclock.data.local_storage.stopwatch.LapDao
import com.piyushprajpti.pyclock.data.local_storage.stopwatch.LapData
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import com.piyushprajpti.pyclock.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun providesDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepository(dataStore)
    }

    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Database(entities = [LapData::class, AlarmData::class], version = 1)
    abstract class PYClockDatabase : RoomDatabase() {
        abstract fun lapDao(): LapDao
        abstract fun alarmDao(): AlarmDao
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): PYClockDatabase {
        return Room.databaseBuilder(context, PYClockDatabase::class.java, "py_clock").build()
    }

    @Singleton
    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Singleton
    @Provides
    fun providesAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }
}