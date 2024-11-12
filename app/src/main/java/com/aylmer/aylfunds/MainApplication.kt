package com.aylmer.aylfunds

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.workers.DailyInterest
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AylFundsApp : Application()

    , Configuration.Provider {

    @Inject
    lateinit var workerFactory : CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}



class CustomWorkerFactory @Inject constructor(private val mainRepo:MainRepository): WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = DailyInterest(
                        appContext,
                        workerParameters,
                        mainRepo,
                        )
}


