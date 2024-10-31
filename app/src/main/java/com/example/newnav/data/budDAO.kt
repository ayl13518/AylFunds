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

}