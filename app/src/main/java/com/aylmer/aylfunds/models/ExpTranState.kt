package com.aylmer.aylfunds.models

import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.utils.convertMillisToDate
import java.util.Calendar


data class ExpTranState(
    val expTrans: List<ExpTrans> = emptyList(),
    val amount: Double = 0.0,
    val dateTrans: String = convertMillisToDate(Calendar.getInstance().timeInMillis),
    val accName: String = "",
    val budName: String = "",
    val tranType: String = "",
    val note: String = "",
    val categoryList: List<String> = emptyList(),
    val accountList: List<String> = emptyList(),
    val typeList: List<String> = TransactionType.entries.map { it.name },
    val tmpAmount: String = "0.00",
    val selectedType: Int = 0,
    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH),

    val tranId: Long=0,
    val accNameTo: String = "",

    val budgetId: Long = 0L,
    val accountId: Long = 0L,
    val accountToId: Long = 0L,
)

//data class CurrentTransactions(
//    val currentTransactions: List<ExpTrans> = emptyList(),
//)
//
//data class ResultTransactions(
//    val resultTransactions: List<ExpTrans> = emptyList(),
//)


data class RollingList(
    val accName: String,
    val amount: Double,
    val balance: Double,
    val dateTrans: String,
    val id: Long,

    val tranType: String="",
    val budName: String="",
    val note: String="",
    val accNameTo: String="",
)

data class BalanceList(
    val accName: String,
    val balance: Double,
    val rolling: Double,
)