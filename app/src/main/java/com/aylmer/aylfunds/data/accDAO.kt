package com.aylmer.aylfunds.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface accDAO {

    @Upsert
    suspend fun upsertAccount(accounts: accounts)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<accounts>>

    @Query("SELECT name FROM accounts ORDER BY " +
            "CASE " +
            "WHEN accType = 'Cash' THEN 1 " +
            "WHEN accType = 'Bank' THEN 2 " +
            "ELSE 3 END, " +
            "balance DESC")
    fun getAllAccountName(): Flow<List<String>>

    @Query("UPDATE accounts SET balance = balance + :amount WHERE name = :name")
    suspend fun updateAccountBalance(name: String, amount: Double)

    @Query("SELECT * FROM accounts WHERE `id` = :accountId")
    fun getAccountById(accountId: Long): Flow<accounts>

    @Query("SELECT balance FROM accounts WHERE `name` = :accName")
    fun getAccountByName(accName: String): Double

    @Query("SELECT balance FROM accounts WHERE `id` = :accountId")
    fun getAccountBalance(accountId: Long): Double

    @Query("UPDATE accounts " +
            "SET balance = balance + (Select amount * (CASE WHEN tranType = 'Expense' THEN 1 ELSE -1 END) FROM expTrans where id = :id)" +
            "WHERE name = (SELECT accName FROM expTrans WHERE id = :id)")
    suspend fun updateOldAccountBalance(id: Long)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccount(id: Long)

    @Query("UPDATE accounts " +
            "SET balance = balance + (Select amount FROM transfer_transactions where id = :id)" +
            "WHERE `id` = (SELECT accountId FROM transfer_transactions WHERE id = :id)")
    suspend fun updateOldAccountFrom(id: Long)

    @Query("UPDATE accounts " +
            "SET balance = balance - (Select amount FROM transfer_transactions where id = :id)" +
            "WHERE `id` = (SELECT accountToId FROM transfer_transactions WHERE id = :id)")
    suspend fun updateOldAccountTo(id: Long)

}