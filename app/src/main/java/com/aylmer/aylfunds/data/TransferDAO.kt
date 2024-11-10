package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferDAO {
    //transfer transactions
    @Query("SELECT * FROM transfer_transactions WHERE cast(STRFTIME('%m', dateTrans) as integer) = :id")
    fun getTransferByMonth(id: Int): Flow<List<TransferTransactions>>

    @Upsert
    suspend fun upsertTransferTransaction(transferTransaction: TransferTransactions)

    @Query("SELECT * FROM transfer_transactions WHERE id = :id")
    fun getTransferTransactionById(id: Long): Flow<TransferTransactions>

    @Query("DELETE FROM transfer_transactions WHERE id = :id")
    suspend fun deleteTransferTransaction(id: Long)
}