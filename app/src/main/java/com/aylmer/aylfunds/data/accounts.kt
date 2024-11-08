package com.aylmer.aylfunds.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class accounts(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val accType: String = "",
    val balance: Double = 0.00,
    val description: String = "",
)
