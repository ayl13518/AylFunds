package com.aylmer.aylfunds.di


import com.aylmer.aylfunds.data.Preferences
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.data.TransferTransactions
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    //Accounts
    suspend fun updateAccountBalance(expTrans: ExpTrans)

    suspend fun updateAccountBalance(transaction: TransferTransactions)

    fun getAllAccounts(): Flow<List<accounts>>

    suspend fun upsertAccount(accounts: accounts)

    fun getAllAccountName(): Flow<List<String>>

    fun getAccountById(accountId: Long): Flow<accounts>

    fun getAccountByName(accName: String): Double

    suspend fun updateOldBalance( id: Long)

    suspend fun deleteAccount(id: Long)

    //Budgets
    fun getAllBudgets(): Flow<List<budgets>>

    suspend fun upsertBudget(budgets: budgets)

    fun getBudgetById(id: Long): Flow<budgets>

    fun getAllCategory(): Flow<List<String>>

    fun getCategoryByType(type: String): Flow<List<String>>

    suspend fun deleteBudget(id: Long)


    //Transactions
    fun getExpByMonth(month: Int): Flow<List<ExpTrans>>

    fun getExpMonthYear(month: Int,year: Int): Flow<List<ExpTrans>>

    fun getTransactionById(tranId: Long): Flow<ExpTrans>

    suspend fun deleteTransaction(id: Long)

    fun getAllTransactions(): Flow<List<ExpTrans>>

    suspend fun upsertExpTran(expTrans: ExpTrans)


    //preference
    fun getAllPreference(): Flow<List<Preferences>>

    fun getPrefName(keyValue: String): Flow<String>

    suspend fun updatePref(expTrans: ExpTrans)

    suspend fun updatePref(keyValue: String, name: String)


    //Transfers
    fun getTransferByMonth(id: Int): Flow<List<TransferTransactions>>

    suspend fun upsertTransferTransaction(transferTransaction: TransferTransactions)

    fun getTransferTransactionById(id: Long): Flow<TransferTransactions>

    suspend fun deleteTransferTransaction(id: Long)

    fun getAllTransfer(): Flow<List<TransferTransactions>>

    //Schedule
    fun getAllSchedule(): Flow<List<Schedule>>

    suspend fun upsertSchedule(schedule: Schedule)

    fun getScheduleById(id: Long): Flow<Schedule>

    suspend fun deleteSchedule(id: Long)

    fun getScheduleDay(day: String): List<Schedule>

}