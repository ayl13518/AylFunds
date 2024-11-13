package com.aylmer.aylfunds.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder

import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerRepository(context: Context) : WorkRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun applyInterest(){
//        val interestWorker = PeriodicWorkRequestBuilder<DailyInterest>(
//            //1, TimeUnit.DAYS,
//            1, TimeUnit.MINUTES,
//            15, TimeUnit.MINUTES
//        )
//        workManager.enqueueUniquePeriodicWork("interest",ExistingPeriodicWorkPolicy.UPDATE,interestWorker.build())

        val interestWorker = OneTimeWorkRequestBuilder<DailyInterest>().build()
        workManager.enqueue(interestWorker)

    }
}