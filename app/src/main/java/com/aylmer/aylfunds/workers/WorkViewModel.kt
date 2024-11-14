package com.aylmer.aylfunds.workers

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.utils.convertMillisToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.util.Calendar
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope

//@HiltViewModel
//class WorkViewModel @Inject constructor(
//    private val workRepo: WorkRepository,
//): ViewModel() {
//
//
//    fun onLoadSchedule(){
//            workRepo.applyInterest()
//    }
//
//}

//@HiltViewModel
//class WorkViewModel @Inject constructor(
//    private val workManagerRepository: WorkRepository,
//): ViewModel()
//{
//    fun onBackup()
//    {
////       workManagerRepository.backupDatabase()
////        val worker = WorkManagerRepository()
////        worker.backupDatabase()
//    }
//}

@Composable
fun BackupButton()
{
    Button(onClick = {
       // viewModel.onBackup()
    })
    {
        Text("Backup Database")
    }
}