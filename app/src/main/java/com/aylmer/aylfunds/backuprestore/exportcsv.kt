package com.aylmer.aylfunds.backuprestore

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.aylmer.aylfunds.di.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class ExportCSV @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainRepo: MainRepository
) {

    val exportDir = File(context.filesDir, "export")

    fun checkDir() {
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
    }


    fun formatExportData(dataFields: List<String>, account: String) : String {
        val reg = Regex("[()=]")
        var strData = account.toString()

        dataFields.forEach { field ->
            strData = strData.replace(field, "")
        }
        strData = strData.replace("name=", "")
        strData = strData.replace(reg, "")
        return strData
    }

    suspend fun doExportAccounts(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {
        val accounts = mainRepo.getAllAccounts()
        val file = File(exportDir, "accounts.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        try {

            accounts.collect {
                it.forEach { account ->
                    var strData = formatExportData(
                        Account_List.entries.map { it.name },
                        account.toString(),
                    )
                    writer.write(strData)
                    writer.write(System.lineSeparator())
                    //println(strData)
                }
                //println("done writing")
                writer.flush()
                writer.close()
                scope.launch { snackBarHostState.showSnackbar("Done Accounts Backup")    }
                return@collect
            }
        }
        catch(throwable: Throwable) {
            println("error $throwable")
        }
    }

    suspend fun doExportBudgets(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {
        val accounts = mainRepo.getAllBudgets()
        val file = File(exportDir, "budgets.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        accounts.collectLatest {
            it.forEach { account ->
                var strData = formatExportData(
                    Budget_List.entries.map { it.name },
                    account.toString(),
                )
                writer.write(strData)
                writer.write(System.lineSeparator())
                //println(strData)
            }
            //println("done writing")
            writer.flush()
            writer.close()
            scope.launch { snackBarHostState.showSnackbar("Done Budgets Backup")    }
        }
    }

    suspend fun doExportTransactions(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {
        val accounts = mainRepo.getAllTransactions()
        val file = File(exportDir, "transactions.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        accounts.collectLatest {
            it.forEach { account ->
                var strData = formatExportData(
                    Transaction_List.entries.map { it.name },
                    account.toString(),
                )
                writer.write(strData)
                writer.write(System.lineSeparator())
                //println(strData)
            }
            //println("done writing transactions")
            writer.flush()
            writer.close()
            scope.launch { snackBarHostState.showSnackbar("Done Transactions Backup")    }
        }
    }

    suspend fun doExportTransfer(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {
        val accounts = mainRepo.getAllTransfer()
        val file = File(exportDir, "transfer.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        accounts.collectLatest {
            it.forEach { account ->
                var strData = formatExportData(
                    Transfer_List.entries.map { it.name },
                    account.toString(),
                )
                writer.write(strData)
                writer.write(System.lineSeparator())
                //println(strData)
            }
            //println("done writing")
            writer.flush()
            writer.close()
            scope.launch { snackBarHostState.showSnackbar("Done Transfer Backup")    }
        }
    }

    suspend fun doExportSchedules(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {
        val accounts = mainRepo.getAllSchedule()
        val file = File(exportDir, "schedules.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        accounts.collectLatest {
            it.forEach { account ->
                var strData = formatExportData(
                    Schedule_List.entries.map { it.name },
                    account.toString(),
                )
                writer.write(strData)
                writer.write(System.lineSeparator())
            }
            writer.flush()
            writer.close()
            scope.launch { snackBarHostState.showSnackbar("Done Schedules Backup")    }
        }
    }


}




