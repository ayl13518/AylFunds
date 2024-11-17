package com.aylmer.aylfunds.di


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.PrefDAO
import com.aylmer.aylfunds.data.Preferences
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.data.ScheduleDAO
import com.aylmer.aylfunds.data.TransferDAO
import com.aylmer.aylfunds.data.TransferTransactions
import com.aylmer.aylfunds.data.accDAO
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budDAO
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.expDAO

@Database(
    entities = [
        ExpTrans::class,
        accounts::class,
        budgets::class,
        Preferences::class,
        TransferTransactions::class,
        Schedule::class
        ],
    version = 9,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        //AutoMigration(from = 8, to = 9),
        ],
    exportSchema = true
)
abstract class BudgetDatabase: RoomDatabase() {
    abstract fun expDao(): expDAO
    abstract fun accDao(): accDAO
    abstract fun budDao(): budDAO
    abstract fun prefDao(): PrefDAO
    abstract fun transferDao(): TransferDAO
    abstract fun scheduleDao(): ScheduleDAO
}