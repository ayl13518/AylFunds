package com.example.newnav.di

import javax.inject.Inject
import javax.inject.Singleton
import com.example.newnav.data.expDAO
import com.example.newnav.data.accDAO
import com.example.newnav.data.accounts
import com.example.newnav.data.budDAO
import com.example.newnav.data.budgets
import com.example.newnav.data.expTrans
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


@Singleton
class DefaultMainRepository @Inject constructor(
    private val expDao: expDAO,
    private val accDAO: accDAO,
    private val budDAO: budDAO
): MainRepository  {

    override suspend fun updateAccountBalance(expTrans: expTrans) {
        if (expTrans.accName != "") {
            if(expTrans.tranType=="Expense")
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount*(-1))
            else if(expTrans.tranType=="Transfer" ) {
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount * (-1))
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount)
            }
            else
                accDAO.updateAccountBalance(expTrans.accName, expTrans.amount)
        }
        expDao.insertExpTran(expTrans)
    }

    override fun getAllAccounts(): Flow<List<accounts>> {
        return accDAO.getAllAccounts()
    }

    override suspend fun insertAccount(accounts: accounts) {
        accDAO.insertAccount(accounts)
    }

    override fun getAllBudgets(): Flow<List<budgets>> {
        return budDAO.getAllBudgets()
    }

    override suspend fun insertBudget(budgets: budgets) {
        budDAO.insertBudget(budgets)
    }

    override fun getAllCategory(): Flow<List<String>> {
        return budDAO.getAllCategory()
    }

    override fun getAllAccountName(): Flow<List<String>> {
        return accDAO.getAllAccountName()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getExpByMonth(month: Int): Flow<List<expTrans>> {
        return expDao.getExpByMonth(month)
//        val result = expDao.getExpByMonth(month)

//        val resultFlow = result
//            .mapLatest { it.toSet() }
//            .distinctUntilChanged()
//            .flatMapLatest {
//                expDao.getExpByMonth(month)
//            }
//
//        val toReturn = ResultTransactions(
//                resultTransactions = resultFlow
//        )
//
//        return toReturn
    }

}