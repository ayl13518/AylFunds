package com.example.newnav.models


data class UserData(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = false,
    val accountList: List<String> = emptyList(),
    val defaultAccount: String? = null,
    val expenseCategory: String? = null,
    val incomeCategory: String? = null,

)

