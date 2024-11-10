package com.aylmer.aylfunds.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.TransactionType


class DailyInterest(appContext: Context,
                    workerParams: WorkerParameters,
                    private val mainRepo: MainRepository,
    ):
    CoroutineWorker(appContext, workerParams)
{

    override suspend fun doWork(): Result {

        try {
            val newExp = ExpTrans(
                id = 0,
                amount = 10.0,
                dateTrans = "2024-11-11",
                budName = "Interest",
                accName = "maya",
                tranType = TransactionType.Income.name,
                note = TransactionType.Income.name
            )
            mainRepo.updateAccountBalance(newExp)
            return Result.success()
        }
        catch(throwable: Throwable) {
            return Result.failure()
        }
    }
}