package com.piyushprajpti.pyclock.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.piyushprajpti.pyclock.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("datastore") },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() })
        )
    }
}