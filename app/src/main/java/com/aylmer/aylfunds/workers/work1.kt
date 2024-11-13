package com.aylmer.aylfunds.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.utils.addDaysToDate
import com.aylmer.aylfunds.utils.convertDateForDB
import com.aylmer.aylfunds.utils.convertMillisToDate
import com.aylmer.aylfunds.utils.convertToLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.time.LocalDate
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

                    var curDate = convertToLocalDate(sched.dateTrans)
                    var strDate = addDaysToDate(curDate, 1, sched.period)

                    val newSched = Schedule(
                        id = sched.id,
                        amount = sched.amount,
                        dateTrans = strDate,
                        budName = sched.budName,
                        accName = sched.accName,
                        tranType = sched.tranType,
                        note = sched.note,
                        period = sched.period
                    )
                    mainRepo.upsertSchedule(newSched)

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