package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface expDAO {

    @Upsert
    suspend fun upsertExpTran(expTrans: ExpTrans)

    @Query("SELECT * FROM expTrans")
    fun getAllExpTrans(): Flow<List<ExpTrans>>

    @Query("DELETE FROM expTrans WHERE id = :id")
    suspend fun deleteExpTrans(id: Int)

    @Query(  "SELECT * FROM expTrans WHERE cast(STRFTIME('%m', dateTrans) as integer) = :id")
    fun getExpByMonth(id: Int): Flow<List<ExpTrans>>

    @Query("SELECT * FROM expTrans WHERE `id` = :tranId")
    fun getTransactionById(tranId: Long): Flow<ExpTrans>

    @Query("DELETE FROM expTrans WHERE id = :id")
    suspend fun deleteTransaction(id: Long)


}