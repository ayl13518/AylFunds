package com.example.newnav.viewmodels

import com.example.newnav.data.expTrans


data class UserData(
    val userTransactions: Set<List<expTrans>> ,
)
