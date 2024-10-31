package com.example.newnav.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class budgets(
    @PrimaryKey(autoGenerate = true)
    val budgetid: Long = 0,
    val name: String = "",
    val balance: Double = 0.00,
    val type: String = "",
    val scope: String = "",
)
