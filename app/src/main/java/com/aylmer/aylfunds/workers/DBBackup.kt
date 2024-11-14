package com.aylmer.aylfunds.workers

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import java.util.zip.ZipInputStream

//@HiltWorker
//class BackUpWorker @AssistedInject constructor (
//    @Assisted    appContext: Context,
//    @Assisted    workerParams: WorkerParameters,
//): CoroutineWorker(appContext, workerParams) {
//
//    override suspend fun doWork(): Result {
//        try {
//
//            val backupFile = File(applicationContext.filesDir, "backup.zip")
//            val databaseFile = File(applicationContext.filesDir, "database.db")
//
//            // Backup
//            backupDatabase(databaseFile, backupFile)
//            return Result.success()
//        } catch (throwable: Throwable) {
//            return Result.failure()
//        }
//    }
//
//    private fun backupDatabase(databaseFile: File, backupFile: File) {
//        ZipOutputStream(FileOutputStream(backupFile)).use { zipOut ->
//            zipOut.putNextEntry(ZipEntry("database.db"))
//            FileInputStream(databaseFile).use { fileIn ->
//                fileIn.copyTo(zipOut)
//                zipOut.closeEntry()
//            }
//        }
//    }
//}


class BackUpWorker2 @Inject constructor (
    @ApplicationContext private val context: Context
)
{
    fun doBackUp() {
        try {

            val backupFile = File(context.filesDir, "backup.zip")
            val databaseFile = File(context.filesDir, "budgetdatabase.db")

            // Backup
            backupDatabase(databaseFile, backupFile)


            //File(database.openHelper.writableDatabase.path)

        } catch (throwable: Throwable) {

        }
    }

    private fun backupDatabase(databaseFile: File, backupFile: File) {
        ZipOutputStream(FileOutputStream(backupFile)).use { zipOut ->
            zipOut.putNextEntry(ZipEntry(databaseFile.name))
            FileInputStream(databaseFile).use { fileIn ->
                fileIn.copyTo(zipOut)
            }
            zipOut.closeEntry()
        }
    }

    fun doRestore(theFile: String){

        val dbFile = context.getDatabasePath("budgetdatabase.db")
        val backupFile = File(context.filesDir, theFile)
        ZipInputStream(FileInputStream(backupFile)).use { zipIn ->
            var entry = zipIn.nextEntry
            while (entry != null) {
                if (entry.name == dbFile.name) {
                    FileOutputStream(dbFile).use {fileOut ->
                        zipIn.copyTo(fileOut)
                    }
                    zipIn.closeEntry()
                    break
                }
                entry = zipIn.nextEntry
            }
        }

    }
}

