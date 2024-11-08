package com.aylmer.aylfunds.transactions

import com.aylmer.aylfunds.data.expTrans
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.CurrentTransactions
import com.aylmer.aylfunds.models.ResultTransactions
//import com.example.newnav.repository.UserRepository
import com.aylmer.aylfunds.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCurrentTransactions @Inject constructor(

    private val mainRepo: MainRepository,
    //private val userRepository: UserRepository
) {

    operator fun invoke(
        searchQuery: Int,
    ): Flow<List<expTrans>> =
        mainRepo.getExpByMonth(searchQuery)
//            .mapToCurrentTransactions( userRepository.userData)

}

private fun Flow<ResultTransactions>.mapToCurrentTransactions(userDataStream: Flow<UserData>): Flow<CurrentTransactions> =
    combine(userDataStream) { searchResult, userData ->
        CurrentTransactions(
            currentTransactions = searchResult.resultTransactions.map { transaction ->
                expTrans(
                    id = transaction.id,
                    amount = transaction.amount,
                    dateTrans = transaction.dateTrans,
                    accName = transaction.accName,
                    budName = transaction.budName,
                    tranType = transaction.tranType,
                    note = transaction.note,
                )
            }
        )
    }