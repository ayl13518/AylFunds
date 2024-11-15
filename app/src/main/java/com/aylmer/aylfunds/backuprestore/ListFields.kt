package com.aylmer.aylfunds.backuprestore

enum class Account_List {
    accounts,
    id,

    //name,
    accType,
    balance,
    description,
}

enum class Budget_List {
    budgets,
    budgetid,

    //    name,
    balance,
    type,
    scope,
}

enum class Transaction_List {
    ExpTrans,
    id,
    amount,
    dateTrans,
    accName,
    budName,
    tranType,
    note,
}

enum class Transfer_List {
    TransferTransactions,
    id,
    amount,
    dateTrans,
    accName,
    accNameTo,
    tranType,
    note,
}

enum class Schedule_List {
    Schedule,
    id,
    amount,
    dateTrans,
    accName,
    budName,
    tranType,
    note,
    period,
    dateNext,
    computeType,
    computePercent,
    taxPercent,
}