package com.aylmer.aylfunds.budgets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.BudgetState
import com.aylmer.aylfunds.transactions.GetCurrentTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())

    private val _budgets = repo.getAllBudgets()

    //val transMonthList = mutableStateOf(ExpTrans())

    val state = combine(
        _state,
        _budgets
    ) { state, budgets ->
        state.copy(
            budgets = budgets,
            name = state.name,
            balance = state.balance,
            type = state.type,
            scope = state.scope,
            tmpBalance = state.tmpBalance,
            selectedMonth = state.selectedMonth,
            selectedYear = state.selectedYear,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetState()
    )

    val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)

    val searchQuery =
        savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = selectedMonth)
    val searchYear =
        savedStateHandle.getStateFlow(key = SEARCH_YEAR, initialValue = _state.value.selectedYear)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = searchQuery.flatMapLatest { query ->
        getCurrentMonth(query + 1, searchYear.value)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    //TODO: Add Event(s)

    fun onSwipe(newMonth: Int) {
        if (newMonth >= 0 && newMonth <= 11) {
            _state.update {
                it.copy(
                    selectedMonth = newMonth
                )
            }
            savedStateHandle[SEARCH_QUERY] = newMonth
        } else if (newMonth >= 12) {
            _state.update {
                it.copy(
                    selectedMonth = 0
                )
            }
            savedStateHandle[SEARCH_QUERY] = 0
        } else {
            _state.update {
                it.copy(
                    selectedMonth = 11
                )
            }
            savedStateHandle[SEARCH_QUERY] = 11
        }
    }


}

private const val SEARCH_QUERY = "searchQuery"
private const val SEARCH_YEAR = "searchYear"