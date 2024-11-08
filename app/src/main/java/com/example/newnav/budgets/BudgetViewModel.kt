package com.example.newnav.budgets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.budgets
import com.example.newnav.di.MainRepository
import com.example.newnav.models.BudgetState
import com.example.newnav.transactions.GetCurrentTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repo: MainRepository,
    savedStateHandle: SavedStateHandle,
    getcurrentMonth: GetCurrentTransactions,
): ViewModel() {

    private val _state = MutableStateFlow(BudgetState())

    private val _budgets = repo.getAllBudgets()

    val state =  combine(
        _state,
        _budgets
    ) { state, budgets ->
        state.copy(
            budgets = budgets,
            name = state.name,
            balance = state.balance,
            type = state.type,
            scope = state.scope,
            tmpBalance = state.tmpBalance
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetState()
    )

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = 10)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = searchQuery.flatMapLatest { query ->
        getcurrentMonth(query + 1)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    //TODO: Add Event(s)


}

private const val SEARCH_QUERY = "searchQuery"