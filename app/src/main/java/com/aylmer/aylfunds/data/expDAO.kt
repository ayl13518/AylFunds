package com.aylmer.aylfunds.data

import androidx.room.Dao

import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface expDAO {

    @Upsert
    suspend fun upsertExpTran(expTrans: ExpTrans): Long

    @Query( "SELECT * FROM expTrans")
    fun getAllExpTrans(): Flow<List<ExpTrans>>

    @Query( "DELETE FROM expTrans WHERE id = :id")
    suspend fun deleteExpTrans(id: Int)

    @Query( "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :id")
    fun getExpByMonth(id: Int): Flow<List<ExpTrans>>

    @Query( "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :month " +
            "AND cast(STRFTIME('%Y', dateTrans) as integer) = :year")
    fun getExpMonthYear(month: Int,year: Int): Flow<List<ExpTrans>>

    @Query( "SELECT * FROM expTrans WHERE `id` = :tranId")
    fun getTransactionById(tranId: Long): Flow<ExpTrans>

    @Query( "DELETE FROM expTrans WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query( "UPDATE expTrans SET budgetId = (SELECT budgetid FROM budgets WHERE name = budName), " +
            "accountId = (SELECT id FROM accounts WHERE name = accName) " +
            "WHERE id = :id ")
    suspend fun updatedId(id: Long)

    @Query( "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :month " +
            "AND cast(STRFTIME('%Y', dateTrans) as integer) = :year " +
            "AND budgetId= :budgetId")
    fun getExpByBudget(month: Int,year: Int,budgetId: Long): Flow<List<ExpTrans>>

    @Query( "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :month " +
            "AND cast(STRFTIME('%Y', dateTrans) as integer) = :year " +
            "AND accountId= :accountId")
    fun getExpByAccount(month: Int,year: Int,accountId: Long): Flow<List<ExpTrans>>



}


