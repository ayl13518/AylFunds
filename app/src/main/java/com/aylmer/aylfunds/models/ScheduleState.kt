package com.aylmer.aylfunds.models

import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.utils.convertMillisToDate
import java.util.Calendar


data class ScheduleState(
    val schedule: List<Schedule> = emptyList(),
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

    val id: Long=0,
    val accNameTo: String = "",
    val period: String = "",
)

