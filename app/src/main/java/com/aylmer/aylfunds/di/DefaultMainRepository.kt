package com.aylmer.aylfunds.di

import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.PrefDAO
import com.aylmer.aylfunds.data.Preferences
import com.aylmer.aylfunds.data.PrevMonth
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.data.ScheduleDAO
import com.aylmer.aylfunds.data.TransferDAO
import com.aylmer.aylfunds.data.TransferTransactions
import com.aylmer.aylfunds.data.accDAO
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budDAO
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.expDAO
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultMainRepository @Inject constructor(
    private val expDao: expDAO,
    private val accDAO: accDAO,
    private val budDAO: budDAO,
    private val prefDao: PrefDAO,
    private val transferDAO: TransferDAO,
    private val scheduleDAO: ScheduleDAO

): MainRepository {

    //Accounts
    override suspend fun updateAccountBalance(expTrans: ExpTrans) {
        if (expTrans.accName != "") {
            if (expTrans.tranType == "Expense")
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount * (-1))
            else
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount)
        }
        val id = expDao.upsertExpTran(expTrans)
        expDao.updatedId(if(id>0) id else expTrans.id)
    }

    override suspend fun updateAccountBalance(transaction: TransferTransactions)
    {
        accDAO.updateAccountBalance(transaction.accName, transaction.amount * (-1))
        accDAO.updateAccountBalance(transaction.accNameTo, transaction.amount)
        val id =transferDAO.upsertTransferTransaction(transaction)
        transferDAO.updateId(if(id>0) id else transaction.id)
    }

    override suspend fun updateOldBalance(id : Long) {
        return accDAO.updateOldAccountBalance(id)
    }

    override fun getAllAccounts(): Flow<List<accounts>> {
        return accDAO.getAllAccounts()
    }

    override suspend fun upsertAccount(accounts: accounts) {
        accDAO.upsertAccount(accounts)
    }

    override fun getAccountById(accountId: Long): Flow<accounts> {
        return accDAO.getAccountById(accountId)
    }

    override fun getAllAccountName(): Flow<List<String>> {
        return accDAO.getAllAccountName()
    }

    override fun getAccountByName(accName: String): Double {
        return accDAO.getAccountByName(accName)
    }

    override suspend fun deleteAccount(id: Long) {
        accDAO.deleteAccount(id)
    }


    //Budgets
    override fun getAllBudgets(): Flow<List<budgets>> {
        return budDAO.getAllBudgets()
    }

    override suspend fun upsertBudget(budgets: budgets) {
        budDAO.upsertBudget(budgets)
    }

    override fun getAllCategory(): Flow<List<String>> {
        return budDAO.getAllCategory()
    }

    override fun getCategoryByType(type: String): Flow<List<String>> {
        return budDAO.getCategoryByType(type)
    }

    override fun getBudgetById(id: Long): Flow<budgets> {
        return budDAO.getBudgetById(id)

    }

    override suspend fun deleteBudget(id: Long) {
        budDAO.deleteBudget(id)
    }


    //Transactions
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getExpByMonth(month: Int): Flow<List<ExpTrans>> {
        return expDao.getExpByMonth(month)
    }

    override fun getExpMonthYear(month: Int,year: Int): Flow<List<ExpTrans>> {
        return expDao.getExpMonthYear(month,year)
    }

    override fun getTransactionById(tranId: Long): Flow<ExpTrans> {
        return expDao.getTransactionById(tranId)
    }

    override suspend fun deleteTransaction(id: Long) {
        accDAO.updateOldAccountBalance(id)
        expDao.deleteTransaction(id)
    }

    override fun getAllTransactions(): Flow<List<ExpTrans>> {
        return expDao.getAllExpTrans()
    }

    override suspend fun upsertExpTran(expTrans: ExpTrans) {
        val id = expDao.upsertExpTran(expTrans)
        expDao.updatedId(if(id>0) id else expTrans.id)
    }

    override fun getExpByType(month: Int,year: Int, id: Long, type: String): Flow<List<ExpTrans>> {
        return if (type == "Budget")
            expDao.getExpByBudget(month,year,id)
        else
            expDao.getExpByAccount(month,year,id)
    }

    override fun getPrevMonth(): Flow<List<PrevMonth>> {
        return  expDao.getPrevMonth()
    }

    //Preferences
    override fun getAllPreference(): Flow<List<Preferences>> {
        return prefDao.getAll()
    }

    override fun getPrefName(keyValue: String): Flow<String> {
        return prefDao.getPrefName(keyValue)
    }

    override suspend fun updatePref(expTrans: ExpTrans) {
        var prefList = listOf(
            if (expTrans.tranType == TransactionType.Expense.name) {
                Preferences(
                    key = PreferenceConfig.ExpenseCategory.keyValue,
                    name = expTrans.budName
                )
            } else if (expTrans.tranType == TransactionType.Expense.name) {
                Preferences(key = PreferenceConfig.IncomeCategory.keyValue, name = expTrans.budName)
            } else {
                Preferences(key = "trancat", name = expTrans.budName)
            },
            Preferences(key = PreferenceConfig.DefaultAccount.keyValue, name = expTrans.accName),
            Preferences(key = PreferenceConfig.TransactionType.keyValue, name = expTrans.tranType),
        )
        prefDao.upsertAll(prefList)
    }

    override suspend fun updatePref(keyValue: String, name: String) {
        prefDao.upsertPref(Preferences(key = keyValue, name = name))
    }


    //Transfers
    override fun getTransferByMonth(id: Int): Flow<List<TransferTransactions>> {
        return transferDAO.getTransferByMonth(id)
    }

    override suspend fun upsertTransferTransaction(transferTransaction: TransferTransactions) {
        val id = transferDAO.upsertTransferTransaction(transferTransaction)
        transferDAO.updateId(if(id>0) id else transferTransaction.id)
    }

    override fun getTransferTransactionById(id: Long): Flow<TransferTransactions> {
        return transferDAO.getTransferTransactionById(id)
    }

    override suspend fun deleteTransferTransaction(id: Long){
        transferDAO.deleteTransferTransaction(id)
    }

    override fun getAllTransfer(): Flow<List<TransferTransactions>> {
        return transferDAO.getAllTransfer()
    }

    override fun getTransferByType(month: Int,year: Int, id: Long, type: String): Flow<List<TransferTransactions>> {
        return if (type == "Budget")
            transferDAO.getTransferByAccount(month,year,0)
        else
            transferDAO.getTransferByAccount(month,year,id)
    }

    override fun getTransferMonthYear(month: Int,year: Int): Flow<List<TransferTransactions>>{
        return transferDAO.getTransferMonthYear(month,year)
    }

    //Schedule
    override fun getAllSchedule(): Flow<List<Schedule>> {
        return scheduleDAO.getAll()
    }

    override suspend fun upsertSchedule(schedule: Schedule){
        return scheduleDAO.upsertSchedule(schedule)
    }

    override fun getScheduleById(id: Long): Flow<Schedule>{
        return scheduleDAO.getScheduleById(id)
    }

    override suspend fun deleteSchedule(id: Long)
    {
        return scheduleDAO.deleteSchedule(id)
    }

    override fun getScheduleDay(day: String): List<Schedule> {
        return scheduleDAO.getScheduleDay(day)
    }

}