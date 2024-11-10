package com.aylmer.aylfunds.workers

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface InterestRepository {

    //val outputWorkInfo: Flow<WorkInfo>

    fun applyInterest()
}