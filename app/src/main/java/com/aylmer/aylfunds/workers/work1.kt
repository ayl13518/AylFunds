package com.aylmer.aylfunds.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.ComputeType
import com.aylmer.aylfunds.models.PeriodType
import com.aylmer.aylfunds.utils.addDaysToDate
import com.aylmer.aylfunds.utils.convertMillisToDate
import com.aylmer.aylfunds.utils.convertToLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class DailyInterest @AssistedInject constructor (
    @Assisted    appContext: Context,
    @Assisted    workerParams: WorkerParameters,
    @Assisted    private val mainRepo: MainRepository,
    ): CoroutineWorker(appContext, workerParams)
{

    override suspend fun doWork(): Result {
        try {
            val schedules = mainRepo.getScheduleDay(
               convertMillisToDate(Calendar.getInstance().timeInMillis))

            if (schedules.isNotEmpty()) {
                schedules.forEach { sched ->
                    var newAmount = sched.amount
                    var balance = mainRepo.getAccountByName(sched.accName)
                    var curDate = convertToLocalDate(sched.dateTrans)
                    var strDate = addDaysToDate(curDate, 1, sched.period)

                    if(sched.computeType == ComputeType.Per_Annum.name){
                        if(sched.period == PeriodType.Daily.name) {
                            newAmount = (balance * (sched.computePercent / 100))/365
                        }
                        else if(sched.period == PeriodType.Weekly.name) {
                            newAmount = (balance * (sched.computePercent / 100))/52
                        }
                        else if(sched.period == PeriodType.SemiMonthly.name) {
                            newAmount = (balance * (sched.computePercent / 100))/26
                        }
                        else if(sched.period == PeriodType.Monthly.name) {
                            newAmount = (balance * (sched.computePercent / 100))/12
                        }
                    }
                    else if(sched.computeType == ComputeType.Percentage.name){
                        newAmount = (balance * (sched.computePercent / 100))
                    }

                    newAmount = newAmount - (newAmount * (sched.taxPercent / 100))

                    val newExp = ExpTrans(
                        id = 0,
                        amount = newAmount,
                        dateTrans = sched.dateTrans,
                        budName = sched.budName,
                        accName = sched.accName,
                        tranType = sched.tranType,
                        note = sched.note,
                    )
                    mainRepo.updateAccountBalance(newExp)

                    val newSched = Schedule(
                        id = sched.id,
                        amount = newAmount,
                        dateTrans = strDate,
                        budName = sched.budName,
                        accName = sched.accName,
                        tranType = sched.tranType,
                        note = sched.note,
                        period = sched.period,
                        computeType = sched.computeType,
                        computePercent = sched.computePercent,
                        taxPercent = sched.taxPercent
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

