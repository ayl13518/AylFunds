package com.example.newnav.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class expTrans(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double = 0.00,
    val dateTrans: String = ""
)
