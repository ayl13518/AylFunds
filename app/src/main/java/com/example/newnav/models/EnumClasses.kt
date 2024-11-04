package com.example.newnav.models

enum class TransactionType {
    Expense, Income, Transfer,
}

enum class AccountType {
    Bank, Cash, Investments
}

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

enum class PreferenceConfig (val keyValue: String, val description: String) {
    ExpenseCategory("expcat", "Default Expense Category"),
    IncomeCategory("inccat", "Default Income Category"),
    DefaultAccount("defacc", "Default Account"),
    TransactionType("trantyp", "Default Transaction Type"),
    UseDarkTheme("dark", "Use Dark Theme"),
}
