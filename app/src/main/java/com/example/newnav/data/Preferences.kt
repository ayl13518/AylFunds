package com.example.newnav.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "preferences"
)
data class Preferences(
    @PrimaryKey val key: String="",
    val name: String="",
)