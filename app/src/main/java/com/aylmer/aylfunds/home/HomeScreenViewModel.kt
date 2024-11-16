package com.aylmer.aylfunds.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.aylmer.aylfunds.di.MainRepository

import com.aylmer.aylfunds.transactions.GetCurrentTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _accounts = mainRepo.getAllAccounts()
    private val _budgets = mainRepo.getAllBudgets()

    private val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = selectedMonth)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val transMonthList = searchQuery.flatMapLatest { query ->
        getCurrentMonth(query + 1)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val state =  combine(
        _state,
        _accounts,
        _budgets,
        transMonthList
    ) { state,
        accounts,
        budgets,
        transMonthList ->
        state.copy(
            accounts = accounts,
            budgets = budgets,
            expTrans = transMonthList
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )
}

private const val SEARCH_QUERY = "searchQuery"