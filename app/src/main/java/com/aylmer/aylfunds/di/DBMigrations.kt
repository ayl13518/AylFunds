package com.aylmer.aylfunds.di

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


internal object  DatabaseMigrations {

    @DeleteTable.Entries(
        DeleteTable(
            tableName = "expTrans",
        )
    )
    class Schema1to2 : AutoMigrationSpec


}

val Migration8to9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //transactions
        database.execSQL("ALTER TABLE expTrans " +
                "ADD COLUMN budgetId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE expTrans " +
                "ADD COLUMN accountId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("UPDATE expTrans SET budgetId = 0, accountId = 0")
        database.execSQL("UPDATE expTrans " +
                "SET budgetId = (SELECT budgetid FROM budgets WHERE name = budName) ")
        database.execSQL("UPDATE expTrans " +
                "SET accountId = (SELECT id FROM accounts WHERE name = accName) ")

        //transfer transactions
        database.execSQL("ALTER TABLE transfer_transactions " +
                "ADD COLUMN accountId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE transfer_transactions " +
                "ADD COLUMN accountToId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("UPDATE transfer_transactions SET accountToId = 0, accountId = 0")
        database.execSQL("UPDATE transfer_transactions " +
                "SET accountToId = (SELECT id FROM accounts WHERE name = accNameTo) ")
        database.execSQL("UPDATE transfer_transactions " +
                "SET accountId = (SELECT id FROM accounts WHERE name = accName) ")

        //schedules
        database.execSQL("ALTER TABLE schedule " +
                "ADD COLUMN budgetId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE schedule " +
                "ADD COLUMN accountId INTEGER NOT NULL DEFAULT 0")
        database.execSQL("UPDATE schedule SET budgetId = 0, accountId = 0")
        database.execSQL("UPDATE schedule " +
                "SET budgetId = (SELECT budgetid FROM budgets WHERE name = budName) ")
        database.execSQL("UPDATE schedule " +
                "SET accountId = (SELECT id FROM accounts WHERE name = accName) ")

    }
}