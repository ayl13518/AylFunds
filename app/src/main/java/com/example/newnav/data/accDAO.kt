package com.example.newnav.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface accDAO {

    @Insert
    suspend fun insertAccount(accounts: accounts)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<accounts>>

    @Query("SELECT name FROM accounts")
    fun getAllAccountName(): Flow<List<String>>

    @Query("UPDATE accounts set balance = balance + :amount WHERE name = :name")
    suspend fun updateAccountBalance(name: String, amount: Double)

}