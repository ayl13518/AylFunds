package com.aylmer.aylfunds.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.utils.convertMillisToDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class DailyInterest
@AssistedInject constructor
    (
  @Assisted    appContext: Context,
 @Assisted    workerParams: WorkerParameters,
  @Assisted    private val mainRepo: MainRepository,
    ):
    CoroutineWorker(appContext, workerParams)
{

    override suspend fun doWork(): Result {
        try {
            val schedules = mainRepo.getScheduleDay(
               convertMillisToDate(Calendar.getInstance().timeInMillis))

            if (schedules.isNotEmpty()) {
                schedules.forEach { sched ->

                    val newExp = ExpTrans(
                        id = 0,
                        amount = sched.amount,
                        dateTrans = sched.dateTrans,
                        budName = sched.budName,
                        accName = sched.accName,
                        tranType = sched.tranType,
                        note = sched.note
                    )
                    mainRepo.updateAccountBalance(newExp)
                }
            }
        println("Hello")
            return Result.success()
        }
        catch(throwable: Throwable) {
            return Result.failure()
        }
    }
}