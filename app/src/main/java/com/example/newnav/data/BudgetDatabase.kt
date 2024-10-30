package com.example.newnav.data


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        expTrans::class,
        accounts::class,
        ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
        //AutoMigration(from = 1, to = 2, spec = DatabaseMigrations.Schema1to2::class)
        ],
    exportSchema = true
)
abstract class BudgetDatabase: RoomDatabase() {

    abstract fun expDao(): expDAO
    abstract fun accDao(): accDAO

}