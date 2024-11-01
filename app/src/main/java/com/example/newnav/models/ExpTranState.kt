package com.example.newnav.models

import com.example.newnav.data.expTrans
import com.example.newnav.utils.convertMillisToDate
import java.util.Calendar

data class ExpTranState(
    val expTrans: List<expTrans> = emptyList(),
    val amount: Double = 0.0,
    val dateTrans: String = convertMillisToDate(Calendar.getInstance().timeInMillis),
    val accName: String = "",
    val budName: String = "",
    val tranType: String = "",
    val note: String = "",
    val categoryList: List<String> = emptyList(),
    val accountList: List<String> = emptyList(),
    val tmpAmount: String = "0.00",
)

