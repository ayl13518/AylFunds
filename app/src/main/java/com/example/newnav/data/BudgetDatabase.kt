package com.example.newnav.data


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        expTrans::class,
        accounts::class,
        budgets::class,
        Preferences::class,
        ],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5)
        //AutoMigration(from = 1, to = 2, spec = DatabaseMigrations.Schema1to2::class)
        ],
    exportSchema = true
)
abstract class BudgetDatabase: RoomDatabase() {
    abstract fun expDao(): expDAO
    abstract fun accDao(): accDAO
    abstract fun budDao(): budDAO
    abstract fun prefDao(): PrefDAO

}