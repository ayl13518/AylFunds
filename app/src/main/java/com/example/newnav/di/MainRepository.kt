package com.example.newnav.di


import com.example.newnav.data.accounts
import com.example.newnav.data.budgets
import com.example.newnav.data.expTrans
import kotlinx.coroutines.flow.Flow

interface MainRepository {


    suspend fun updateAccountBalance(expTrans: expTrans)

    fun getAllAccounts(): Flow<List<accounts>>

    suspend fun insertAccount(accounts: accounts)

    fun getAllBudgets(): Flow<List<budgets>>

    suspend fun insertBudget(budgets: budgets)

    fun getAllCategory(): Flow<List<String>>

    fun getAllAccountName(): Flow<List<String>>

}