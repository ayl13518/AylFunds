package com.aylmer.aylfunds.scheduling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SchedulingViewModel @Inject constructor(
    private val mainRepo: MainRepository

): ViewModel() {

    val allSchedule = mainRepo.getAllSchedule()
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList())

}