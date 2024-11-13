package com.aylmer.aylfunds.workers

import android.content.Context

import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@HiltWorker
class BackUpWorker @AssistedInject constructor (
    @Assisted    appContext: Context,
    @Assisted    workerParams: WorkerParameters,
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {

            val backupFile = File(applicationContext.filesDir, "backup.zip")
            val databaseFile = File(applicationContext.filesDir, "database.db")

            // Backup
            backupDatabase(databaseFile, backupFile)
            return Result.success()
        } catch (throwable: Throwable) {
            return Result.failure()
        }
    }

    private fun backupDatabase(databaseFile: File, backupFile: File) {
        ZipOutputStream(FileOutputStream(backupFile)).use { zipOut ->
            zipOut.putNextEntry(ZipEntry("database.db"))
            FileInputStream(databaseFile).use { fileIn ->
                fileIn.copyTo(zipOut)
                zipOut.closeEntry()
            }
        }
    }
}

