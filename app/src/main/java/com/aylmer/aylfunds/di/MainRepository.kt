package com.aylmer.aylfunds.di


import com.aylmer.aylfunds.data.Preferences
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.expTrans
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    //Accounts
    suspend fun updateAccountBalance(expTrans: expTrans)

    fun getAllAccounts(): Flow<List<accounts>>

    suspend fun upsertAccount(accounts: accounts)

    fun getAllAccountName(): Flow<List<String>>

    fun getAccountById(accountId: Long): Flow<accounts>

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