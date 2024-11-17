package com.aylmer.aylfunds.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "expTrans"
)
data class ExpTrans(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double = 0.00,
    val dateTrans: String = "",
    @ColumnInfo(defaultValue = "Cash")
    val accName: String = "",
    @ColumnInfo(defaultValue = "Others")
    val budName: String = "",
    @ColumnInfo(defaultValue = "Expense")
    val tranType: String = "",
    @ColumnInfo(defaultValue = " ")
    val note: String = "",
    @ColumnInfo(defaultValue = "0")
    val budgetId: Long = 0,
    @ColumnInfo(defaultValue = "0")
    val accountId: Long = 0,
)


