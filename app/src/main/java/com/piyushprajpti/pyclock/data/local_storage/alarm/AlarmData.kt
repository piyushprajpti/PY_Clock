package com.piyushprajpti.pyclock.data.local_storage.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AlarmData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("isOn")
    var isOn: Boolean,

    @ColumnInfo("millis")
    var time: Long
)
