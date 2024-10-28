package com.example.newnav.models

import com.example.newnav.data.expTrans

data class ExpTranState(
    val expTrans: List<expTrans> = emptyList(),
    val amount: Double = 0.0,
    val dateTrans: String = "",
)