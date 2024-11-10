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
)


@Entity(
    tableName = "transfer_transactions"
)
data class TransferTransactions(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double = 0.00,
    val dateTrans: String = "",
    @ColumnInfo(defaultValue = "Cash")
    val accName: String = "",
    @ColumnInfo(defaultValue = "Cash")
    val accNameTo: String = "",
    @ColumnInfo(defaultValue = "Transfer")
    val tranType: String = "Transfer",
    @ColumnInfo(defaultValue = "Transfer")
    val note: String = "Transfer",
)