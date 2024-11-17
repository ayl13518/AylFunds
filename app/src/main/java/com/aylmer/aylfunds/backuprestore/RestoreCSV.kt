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
                    name = linetxt[1].trimStart().toString(),
                    accType = linetxt[2].trimStart().toString(),
                    balance = linetxt[3].toDouble(),
                    description = linetxt[4].trimStart().toString(),
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
                    name = linetxt[1].trimStart().toString(),
                    balance = linetxt[2].toDouble(),
                    type = linetxt[3].trimStart().toString(),
                    scope = linetxt[4].trimStart().toString(),
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
                    dateTrans = linetxt[2].trimStart().toString(),
                    accName = linetxt[3].trimStart().toString(),
                    budName = linetxt[4].trimStart().toString(),
                    tranType = linetxt[5].trimStart().toString(),
                    note = linetxt[6].trimStart().toString(),
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
                    dateTrans = linetxt[2].trimStart().toString(),
                    accName = linetxt[3].trimStart().toString(),
                    accNameTo = linetxt[4].trimStart().toString(),
                    tranType = linetxt[5].trimStart().toString(),
                    note = linetxt[6].trimStart().toString(),
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
                    dateTrans = linetxt[2].trimStart().toString(),
                    accName = linetxt[3].trimStart().toString(),
                    budName = linetxt[4].trimStart().toString(),
                    tranType = linetxt[5].trimStart().toString(),
                    note = linetxt[6].trimStart().toString(),
                    period = linetxt[7].trimStart().toString(),
                    dateNext = linetxt[8].trimStart().toString(),
                    computeType = linetxt[9].trimStart().toString(),
                    computePercent = linetxt[10].toDouble(),
                    taxPercent = linetxt[11].toDouble(),
                )
                mainRepo.upsertSchedule(oldAccounts)
            }
            input.close()
        }
    }
}