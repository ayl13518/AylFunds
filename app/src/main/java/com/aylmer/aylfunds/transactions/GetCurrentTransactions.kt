package com.aylmer.aylfunds.transactions

import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.di.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentTransactions @Inject constructor(
    private val mainRepo: MainRepository,
) {

    operator fun invoke(
        searchQuery: Int,
    ): Flow<List<ExpTrans>> =
        mainRepo.getExpByMonth(searchQuery)
//            .mapToCurrentTransactions( userRepository.userData)

    operator fun invoke(
        searchMonth: Int,
        searchYear: Int,
    ): Flow<List<ExpTrans>> =
        mainRepo.getExpMonthYear(searchMonth, searchYear)

    operator fun invoke(
        searchMonth: Int,
        searchYear: Int,
        id: Long,
        type: String): Flow<List<ExpTrans>> =
        mainRepo.getExpByType(searchMonth, searchYear, id, type)

}

//private fun Flow<ResultTransactions>.mapToCurrentTransactions(userDataStream: Flow<UserData>): Flow<CurrentTransactions> =
//    combine(userDataStream) { searchResult, userData ->
//        CurrentTransactions(
//            currentTransactions = searchResult.resultTransactions.map { transaction ->
//                ExpTrans(
//                    id = transaction.id,
//                    amount = transaction.amount,
//                    dateTrans = transaction.dateTrans,
//                    accName = transaction.accName,
//                    budName = transaction.budName,
//                    tranType = transaction.tranType,
//                    note = transaction.note,
//                )
//            }
//        )
//    }