package com.piyushprajpti.pyclock.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun <T> setValue(ket: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[ket] = value
        }
    }

    fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map {
            it[key]
        }.catch {
            if (it is IOException) emit(null)
            else throw it
        }
    }

}