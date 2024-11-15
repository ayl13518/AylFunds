package com.aylmer.aylfunds.workers

import android.content.Context
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.Account_List
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import javax.inject.Inject


class ExportCSV @Inject constructor (
    @ApplicationContext private val context: Context,
    private val mainRepo: MainRepository
)
{

    val exportDir = File(context.filesDir, "export")

    fun checkDir(){
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
    }

    suspend fun doExportCSV() {

        val accounts = mainRepo.getAllAccounts()

        val file = File(exportDir, "accounts.csv")
        if (file.exists()) file.delete()
        file.createNewFile()

        val writer = file.writer()
        val reg = Regex("[()=]")

        accounts.collectLatest {
            it.forEach { account ->

                var dataFields = Account_List.entries.map { it.name }
                //var mem = dataFields.members
                var strData =account.toString()


                dataFields.forEach { field ->
                    strData = strData.replace(field, "")
                }

                strData = strData.replace("name=", "")
                strData = strData.replace(reg, "")
                writer.write(strData )
                writer.write(System.lineSeparator())
                println(strData)
            }
            println("done writing")
            writer.flush()
            writer.close()
        }

    }

    suspend fun doRestoreCSV(){

        val file = File(exportDir, "accounts.csv")
        if (file.exists()) {
            println(file.length().toString())
            val input = file.reader()

            input.readLines().forEach { txt ->
                println(txt.toString())

                var linetxt =    txt.split(",")
                val oldAccounts = accounts (
                    id = linetxt[0].toLong(),
                    name = linetxt[1].trimStart().toString(),
                    accType = linetxt[2].trimStart().toString(),
                    balance = linetxt[3].toDouble(),
                    description = linetxt[4].trimStart().toString(),
                )
                println(oldAccounts.toString())
                mainRepo.upsertAccount(oldAccounts)
            }
            input.close()
            println("done reading")



        }
    }

}

//    fun exportCSV(filename:String){
//       val file = File(exportDir, filename)
//    file.createNewFile()
//    val csvWriter = CSVWriter(FileWriter(file))
//    val cursor: Cursor = db.query("SELECT * FROM your_table_name", null)
//    csvWriter.writeNext(cursor.columnNames)
//    while (cursor.moveToNext()) {
//        val arrStr = arrayOfNulls<String>(cursor.count)
//        for (i in 0 until cursor.count) {
//            arrStr[i] = cursor.getString(i)
//        }
//        csvWriter.writeNext(arrStr)
//    }
//    csvWriter.close()
//    cursor.close()
//    }



