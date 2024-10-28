package com.example.newnav.models

import com.example.newnav.data.expTrans

data class ExpTranState(
    val expTrans: List<expTrans> = emptyList(),
    val amount: String = "0.00",
)