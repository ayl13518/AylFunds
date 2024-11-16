package com.aylmer.aylfunds.backuprestore

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.di.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    suspend fun doExportAccounts() {
        val accounts = mainRepo.getAllAccounts()
        val file = File(exportDir, "accounts.csv")
        if (file.exists()) file.delete()
        file.createNewFile()
        val writer = file.writer()

        try {

            accounts.collect() {
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
                return@collect
            }
        }
        catch(throwable: Throwable) {
            println("error $throwable")
        }
    }

    suspend fun doExportBudgets() {
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
        }
    }

    suspend fun doExportTransactions()  {
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
        }
    }

    suspend fun doExportTransfer() {
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
        }
    }

    suspend fun doExportSchedules() {
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
        }
    }


}




