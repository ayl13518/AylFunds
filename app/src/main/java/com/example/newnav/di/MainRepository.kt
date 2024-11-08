package com.example.newnav.di


import com.example.newnav.data.Preferences
import com.example.newnav.data.accounts
import com.example.newnav.data.budgets
import com.example.newnav.data.expTrans
import com.example.newnav.models.ResultTransactions
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    //Accounts
    suspend fun updateAccountBalance(expTrans: expTrans)

    fun getAllAccounts(): Flow<List<accounts>>

    suspend fun insertAccount(accounts: accounts)

    fun getAllAccountName(): Flow<List<String>>

    //Budgets
    fun getAllBudgets(): Flow<List<budgets>>

    suspend fun upsertBudget(budgets: budgets)

    fun getBudgetById(id: Long): Flow<budgets>

    fun getAllCategory(): Flow<List<String>>


    //Transactions
    fun getExpByMonth(month: Int): Flow<List<expTrans>>

    fun getCategoryByType(type: String): Flow<List<String>>


    //preference
    fun getAllPreference(): Flow<List<Preferences>>

    fun getPrefName(keyValue: String): Flow<String>

    suspend fun updatePref(expTrans: expTrans)

    suspend fun updatePref(keyValue: String, name: String)



}