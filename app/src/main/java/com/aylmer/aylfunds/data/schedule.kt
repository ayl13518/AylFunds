package com.aylmer.aylfunds.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule"
)
data class Schedule(
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

    val period: String = "",
    val dateNext: String = "",

)