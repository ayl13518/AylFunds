package com.aylmer.aylfunds.di


import com.aylmer.aylfunds.data.Preferences
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.ExpTrans
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    //Accounts
    suspend fun updateAccountBalance(expTrans: ExpTrans)

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
    fun getExpByMonth(month: Int): Flow<List<ExpTrans>>

    fun getTransactionById(tranId: Long): Flow<ExpTrans>

    fun getCategoryByType(type: String): Flow<List<String>>

    suspend fun deleteTransaction(id: Long)


    //preference
    fun getAllPreference(): Flow<List<Preferences>>

    fun getPrefName(keyValue: String): Flow<String>

    suspend fun updatePref(expTrans: ExpTrans)

    suspend fun updatePref(keyValue: String, name: String)



}