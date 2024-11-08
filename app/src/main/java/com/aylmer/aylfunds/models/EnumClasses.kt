package com.aylmer.aylfunds.models

enum class TransactionType {
    Expense, Income, Transfer,
}

enum class AccountType {
    Bank, Cash, Investments
}

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,    LIGHT,    DARK,
}

enum class PreferenceConfig (val keyValue: String, val description: String) {
    ExpenseCategory("expcat", "Default Expense Category"),
    IncomeCategory("inccat", "Default Income Category"),
    DefaultAccount("defacc", "Default Account"),
    TransactionType("trantyp", "Default Transaction Type"),
    UseDarkTheme("dark", "Use Dark Theme"),
}

enum class BudgetType {
    Expense, Income,
}

enum class BudgetScope {
    Month, Week, Year,
}