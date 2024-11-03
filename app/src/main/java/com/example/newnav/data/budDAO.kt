package com.example.newnav.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface budDAO {

    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<budgets>>

    @Insert
    suspend fun insertBudget(budgets: budgets)

    @Query("SELECT name as category FROM budgets")
    fun getAllCategory(): Flow<List<String>>

    @Query("SELECT name as category FROM budgets WHERE type = :type")
    fun getCategoryByType(type: String): Flow<List<String>>


}