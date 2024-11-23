package com.aylmer.aylfunds.home

import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.PrevMonth
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budgets

data class HomeState(
    val accounts: List<accounts> = emptyList(),
    val budgets: List<budgets> = emptyList(),
    val expTrans: List<ExpTrans> = emptyList(),
    val prevMonth: List<PrevMonth> = emptyList(),

)
