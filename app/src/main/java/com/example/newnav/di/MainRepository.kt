package com.example.newnav.di


import com.example.newnav.data.accounts
import com.example.newnav.data.expTrans
import kotlinx.coroutines.flow.Flow

interface MainRepository {


    suspend fun updateAccountBalance(expTrans: expTrans)

    fun getAllAccounts(): Flow<List<accounts>>
}