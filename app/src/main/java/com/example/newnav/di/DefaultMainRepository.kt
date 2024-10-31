package com.example.newnav.di

import javax.inject.Inject
import javax.inject.Singleton
import com.example.newnav.data.expDAO
import com.example.newnav.data.accDAO
import com.example.newnav.data.accounts
import com.example.newnav.data.expTrans
import kotlinx.coroutines.flow.Flow

@Singleton
class DefaultMainRepository @Inject constructor(
    private val expDao: expDAO,
    private val accDAO: accDAO,
): MainRepository  {

    override suspend fun updateAccountBalance(expTrans: expTrans) {
        expDao.insertExpTran(expTrans)
    }

    override fun getAllAccounts(): Flow<List<accounts>> {
        return accDAO.getAllAccounts()
    }

    override suspend fun insertAccount(accounts: accounts) {
        accDAO.insertAccount(accounts)
    }

}