package com.aylmer.aylfunds

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.expDAO
import com.aylmer.aylfunds.di.BudgetDatabase
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.di.Migration8to9
import com.aylmer.aylfunds.utils.convertDateForDB
import dagger.hilt.android.qualifiers.ApplicationContext
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDBMigrationTest {

    private lateinit var mainRepo: expDAO
    private lateinit var db: BudgetDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                BudgetDatabase::class.java,
            )
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .addMigrations(Migration8to9)
                .build()

        mainRepo =db.expDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val newExp = ExpTrans(
            id = 0,
            amount = 100.0,
            dateTrans = "2024-11-12",
            budName = "food",
            accName = "maya",
            tranType = "Expense",
            note = "test"
        )

        //mainRepo.upsertExpTran(newExp)
        //val byName = mainRepo.getTransactionById(1L)

       //assertThat(newExp, equalTo(byName))
    }

}