package com.piyushprajpti.pyclock.data.local_storage.stopwatch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LapDao {

    @Query("SELECT * FROM LapData")
    fun getAllLaps(): List<LapData>

    @Upsert
    fun insertLap(laps: List<LapData>)

    @Query("DELETE FROM LapData")
    fun clearLapData()
}