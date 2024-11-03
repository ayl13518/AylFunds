package com.example.newnav.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class expTrans(
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

fun expTrans.asExternalModel()= expTrans(
    id = id,
    amount = amount,
    dateTrans = dateTrans,
    accName = accName,
    budName = budName,
    tranType = tranType,
    note = note,
)
