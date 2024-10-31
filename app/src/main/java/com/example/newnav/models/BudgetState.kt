package com.example.newnav.models


import com.example.newnav.data.budgets

data class BudgetState(
    val budgets: List<budgets> = emptyList(),
    val name: String = "",
    val balance: Double = 0.00,
    val type: String = "Expense",
    val scope: String = "Month",

    val budTypeList: List<String> = listOf("Expense", "Income"),
    val budScopeList: List<String> = listOf("Month", "Week", "Year")
)
