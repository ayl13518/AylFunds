package com.aylmer.aylfunds.backuprestore

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.data.TransferTransactions
import com.aylmer.aylfunds.di.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class RestoreCSV @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainRepo: MainRepository
) {

    val exportDir = File(context.filesDir, "export")

    suspend fun doRestoreAccounts(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val file = File(exportDir, "accounts.csv")
        if (file.exists()) {
            val input = file.reader()

            input.readLines().forEach { txt ->
                var linetxt = txt.split(",")
                val oldAccounts = accounts(
                    id = linetxt[0].toLong(),
                    name = linetxt[1].trimStart(),
                    accType = linetxt[2].trimStart(),
                    balance = linetxt[3].toDouble(),
                    description = linetxt[4].trimStart(),
                )
                mainRepo.upsertAccount(oldAccounts)
            }
            input.close()
            scope.launch { snackBarHostState.showSnackbar("Done Accounts Restore") }
        }
    }

    suspend fun doRestoreBudgets(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val file = File(exportDir, "budgets.csv")
        if (file.exists()) {
            val input = file.reader()

            input.readLines().forEach { txt ->
                var linetxt = txt.split(",")
                val oldAccounts = budgets(
                    budgetid = linetxt[0].toLong(),
                    name = linetxt[1].trimStart(),
                    balance = linetxt[2].toDouble(),
                    type = linetxt[3].trimStart(),
                    scope = linetxt[4].trimStart(),
                )
                mainRepo.upsertBudget(oldAccounts)
            }
            input.close()
            scope.launch { snackBarHostState.showSnackbar("Done Budgets Restore") }
        }
    }

    suspend fun doRestoreTransactions(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val file = File(exportDir, "transactions.csv")
        if (file.exists()) {
            val input = file.reader()

            input.readLines().forEach { txt ->
                var linetxt = txt.split(",")
                val oldAccounts = ExpTrans(
                    id = linetxt[0].toLong(),
                    amount = linetxt[1].toDouble(),
                    dateTrans = linetxt[2].trimStart(),
                    accName = linetxt[3].trimStart(),
                    budName = linetxt[4].trimStart(),
                    tranType = linetxt[5].trimStart(),
                    note = linetxt[6].trimStart(),
                )
                mainRepo.upsertExpTran(oldAccounts)
            }
            input.close()
        }
    }

    suspend fun doRestoreTransfers(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val file = File(exportDir, "transfer.csv")
        if (file.exists()) {
            val input = file.reader()

            input.readLines().forEach { txt ->
                var linetxt = txt.split(",")
                val oldAccounts = TransferTransactions(
                    id = linetxt[0].toLong(),
                    amount = linetxt[1].toDouble(),
                    dateTrans = linetxt[2].trimStart(),
                    accName = linetxt[3].trimStart(),
                    accNameTo = linetxt[4].trimStart(),
                    tranType = linetxt[5].trimStart(),
                    note = linetxt[6].trimStart(),
                )
                mainRepo.upsertTransferTransaction(oldAccounts)
            }
            input.close()
        }
    }

    suspend fun doRestoreSchedules(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val file = File(exportDir, "schedules.csv")
        if (file.exists()) {
            val input = file.reader()

            input.readLines().forEach { txt ->
                var linetxt = txt.split(",")
                val oldAccounts = Schedule(
                    id = linetxt[0].toLong(),
                    amount = linetxt[1].toDouble(),
                    dateTrans = linetxt[2].trimStart(),
                    accName = linetxt[3].trimStart(),
                    budName = linetxt[4].trimStart(),
                    tranType = linetxt[5].trimStart(),
                    note = linetxt[6].trimStart(),
                    period = linetxt[7].trimStart(),
                    dateNext = linetxt[8].trimStart(),
                    computeType = linetxt[9].trimStart(),
                    computePercent = linetxt[10].toDouble(),
                    taxPercent = linetxt[11].toDouble(),
                )
                mainRepo.upsertSchedule(oldAccounts)
            }
            input.close()
        }
    }
}