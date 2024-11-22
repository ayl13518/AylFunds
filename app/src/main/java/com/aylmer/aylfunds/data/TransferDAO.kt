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
    suspend fun upsertTransferTransaction(transferTransaction: TransferTransactions): Long

    @Query("SELECT * FROM transfer_transactions WHERE id = :id")
    fun getTransferTransactionById(id: Long): Flow<TransferTransactions>

    @Query("DELETE FROM transfer_transactions WHERE id = :id")
    suspend fun deleteTransferTransaction(id: Long)

    @Query("SELECT * FROM transfer_transactions")
    fun getAllTransfer(): Flow<List<TransferTransactions>>

    @Query("UPDATE transfer_transactions SET accountId = (SELECT id FROM accounts WHERE name = accName), " +
            "accountToId = (SELECT id FROM accounts WHERE name = accNameTo) " +
            "WHERE id = :id ")
    suspend fun updateId(id: Long)

    @Query( "SELECT * FROM transfer_transactions WHERE cast(STRFTIME('%m', dateTrans) as integer) = :month " +
            "AND cast(STRFTIME('%Y', dateTrans) as integer) = :year " +
            "AND accountId= :accountId")
    fun getTransferByAccount(month: Int,year: Int,accountId: Long): Flow<List<TransferTransactions>>

    @Query( "SELECT * FROM transfer_transactions WHERE cast(STRFTIME('%m', dateTrans) as integer) = :month " +
            "AND cast(STRFTIME('%Y', dateTrans) as integer) = :year ")
    fun getTransferMonthYear(month: Int,year: Int): Flow<List<TransferTransactions>>

}