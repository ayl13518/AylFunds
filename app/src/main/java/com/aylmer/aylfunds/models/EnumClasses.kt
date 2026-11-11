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
    UseDarkTheme("dark", "Theme Mode"),
    ColorTheme("color", "Color Theme"),
    BudgetScope("budscope", "Default Budget Scope"),
}

enum class BudgetType {
    Expense, Income,
}

enum class BudgetScope {
    Week, Month, Year,
}

enum class PeriodType {
    Daily, Weekly, SemiMonthly, Monthly , Annually,

}

enum class ComputeType {
    Fixed_Amount, Percentage, Per_Annum
}

/** Light/dark mode. Older saves stored "true"/"false" under the same key. */
enum class ThemeMode(val label: String) {
    System("Follow System"), Light("Light"), Dark("Dark");

    companion object {
        fun fromPref(value: String?): ThemeMode = when (value) {
            "true" -> Dark
            "false" -> Light
            else -> entries.find { it.name == value } ?: System
        }
        fun fromLabel(label: String): ThemeMode = entries.find { it.label == label } ?: System
    }
}

/** Color palette applied on top of the light/dark mode. */
enum class ColorTheme(val label: String) {
    Dynamic("Dynamic (Wallpaper)"),
    Default("App Default"),
    Purple("Purple"),
    Green("Green"),
    HighContrast("High Contrast");

    companion object {
        fun fromPref(value: String?): ColorTheme = entries.find { it.name == value } ?: Dynamic
        fun fromLabel(label: String): ColorTheme = entries.find { it.label == label } ?: Dynamic
    }
}
