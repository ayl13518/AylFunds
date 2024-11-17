package com.aylmer.aylfunds.models


import com.aylmer.aylfunds.data.budgets

data class BudgetState(
    val budgets: List<budgets> = emptyList(),
    val name: String = "",
    val balance: Double = 0.00,
    val type: String = "Expense",
    val scope: String = "Month",

    val budTypeList: List<String> = BudgetType.entries.map { it.name },
    val budScopeList: List<String> = BudgetScope.entries.map { it.name },

    val tmpBalance: String = "0.00",


)
