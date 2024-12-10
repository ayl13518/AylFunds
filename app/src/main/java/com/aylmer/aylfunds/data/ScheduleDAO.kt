package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDAO {

    @Query("SELECT * FROM schedule")
    fun getAll(): Flow<List<Schedule>>

    @Upsert
    suspend fun upsertSchedule(schedule: Schedule): Long

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun getScheduleById(id: Long): Flow<Schedule>

    @Query("DELETE FROM schedule WHERE id = :id")
    suspend fun deleteSchedule(id: Long)

    @Query( "SELECT * FROM schedule WHERE dateTrans <= :day")
    fun getScheduleDay(day: String): List<Schedule>

    @Query( "UPDATE schedule SET budgetId = (SELECT budgetid FROM budgets WHERE name = budName), " +
            "accountId = (SELECT id FROM accounts WHERE name = accName) " +
            "WHERE id = :id ")
    suspend fun updatedId(id: Long)

}