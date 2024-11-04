package com.example.newnav.di


import com.example.newnav.data.Preferences
import com.example.newnav.data.accounts
import com.example.newnav.data.budgets
import com.example.newnav.data.expTrans
import com.example.newnav.models.ResultTransactions
import kotlinx.coroutines.flow.Flow

interface MainRepository {


    suspend fun updateAccountBalance(expTrans: expTrans)

    fun getAllAccounts(): Flow<List<accounts>>

    suspend fun insertAccount(accounts: accounts)

    fun getAllBudgets(): Flow<List<budgets>>

    suspend fun insertBudget(budgets: budgets)

    fun getAllCategory(): Flow<List<String>>

    fun getAllAccountName(): Flow<List<String>>

    fun getExpByMonth(month: Int): Flow<List<expTrans>>

    fun getCategoryByType(type: String): Flow<List<String>>

    fun getAllPreference(): Flow<List<Preferences>>

    fun getPrefName(keyValue: String): Flow<String>

    suspend fun updatePref(expTrans: expTrans)

}