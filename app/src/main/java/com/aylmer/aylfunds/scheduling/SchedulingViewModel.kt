package com.aylmer.aylfunds.scheduling

import androidx.lifecycle.ViewModel
import com.aylmer.aylfunds.di.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SchedulingViewModel @Inject constructor(
    private val repo: MainRepository

): ViewModel() {
//TODO: Add StateFlow(s)


}