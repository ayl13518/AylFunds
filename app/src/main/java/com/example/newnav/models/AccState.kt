package com.example.newnav.models

import com.example.newnav.data.accounts

data class AccState(
    val accounts: List<accounts> = emptyList(),
    val name: String = "",
    val accType: String = "",
    val balance: Double = 0.00,
    val description: String = "",
    val accTypeList: List<String> = listOf("Cash", "Bank", "Digital Bank","Loan","Investment")
)
