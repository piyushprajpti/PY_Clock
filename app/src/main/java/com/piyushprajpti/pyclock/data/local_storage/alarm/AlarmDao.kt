package com.piyushprajpti.pyclock.data.local_storage.alarm

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AlarmDao {

    @Query("SELECT * FROM AlarmData")
    fun getAllAlarms(): List<AlarmData>

    @Upsert
    fun upsertAlarm(data: AlarmData)

    @Query("SELECT * FROM alarmdata WHERE id = :alarmId")
    fun getAlarmById(alarmId: Int): AlarmData

    @Query("DELETE FROM AlarmData WHERE id = :alarmId")
    fun deleteAlarm(alarmId: Int)

    @Query("UPDATE AlarmData SET isOn = :isOn WHERE id = :alarmId")
    fun updateAlarmStatus(alarmId: Int, isOn: Boolean)
}