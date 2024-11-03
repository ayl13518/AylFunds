package com.example.newnav.models

import com.example.newnav.data.expTrans
import com.example.newnav.utils.convertMillisToDate
import com.example.newnav.utils.getCurrentDate
import java.time.Month
import java.util.Calendar
import java.util.Date


data class ExpTranState(
    val expTrans: List<expTrans> = emptyList(),
    val amount: Double = 0.0,
    val dateTrans: String = convertMillisToDate(Calendar.getInstance().timeInMillis),
    //val dateTrans: String = getCurrentDate(),
    val accName: String = "",
    val budName: String = "",
    val tranType: String = "",
    val note: String = "",
    val categoryList: List<String> = emptyList(),
    val accountList: List<String> = emptyList(),
    val typeList: List<String> = listOf("Expense", "Income", "Transfer"),
    val tmpAmount: String = "0.00",
    val selectedType: Int = 0,

    //val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    val selectedMonth: Int = 10
)

data class CurrentTransactions(
    val currentTransactions: List<expTrans> = emptyList(),
)

data class ResultTransactions(
    val resultTransactions: List<expTrans> = emptyList(),
)
