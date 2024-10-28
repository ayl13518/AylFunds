package com.example.newnav.data


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [expTrans::class],
        version = 1)
abstract class BudgetDatabase: RoomDatabase() {

    abstract val expDao: expDAO

}