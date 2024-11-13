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

@HiltViewModel
class WorkViewModel @Inject constructor(
    //private val workManagerRepository: WorkManagerRepository,
): ViewModel()
{
    fun onBackup()
    {
        //workManagerRepository.backupDatabase()
    }
}

@Composable
fun BackupButton(viewModel: WorkViewModel = hiltViewModel())
{
    Button(onClick = {
        //viewModel.onBackup()
    })
    {
        Text("Backup Database")
    }
}