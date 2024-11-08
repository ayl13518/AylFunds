package com.aylmer.aylfunds.data

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec


internal object  DatabaseMigrations {
    @DeleteTable.Entries(
        DeleteTable(
            tableName = "expTrans",
        )
    )
    class Schema1to2 : AutoMigrationSpec


}