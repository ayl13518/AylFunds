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

    @Query("SELECT name FROM accounts")
    fun getAllAccountName(): Flow<List<String>>

    @Query("UPDATE accounts set balance = balance + :amount WHERE name = :name")
    suspend fun updateAccountBalance(name: String, amount: Double)

    @Query("SELECT * FROM accounts WHERE `id` = :accountId")
    fun getAccountById(accountId: Long): Flow<accounts>

}