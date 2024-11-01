package com.example.newnav.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface accDAO {

    @Insert
    suspend fun insertAccount(accounts: accounts)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<accounts>>

    @Query("SELECT name FROM accounts")
    fun getAllAccountName(): Flow<List<String>>

}