package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {

    @Query("SELECT * FROM schedule")
    fun getAll(): Flow<List<Schedule>>

}