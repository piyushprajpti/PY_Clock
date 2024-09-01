package com.piyushprajpti.pyclock.data.local_storage.stopwatch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LapData(
    @PrimaryKey
    @ColumnInfo(name = "lap_count")
    val lapCount: Int,

    @ColumnInfo(name = "lap_time")
    val lapTime: String,

    @ColumnInfo(name = "total_time")
    val totalTime: String
)
