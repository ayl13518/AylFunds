package com.aylmer.aylfunds.budgets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.BudgetScope
import com.aylmer.aylfunds.models.BudgetState
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.transactions.GetCurrentTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
//
//    private val _budgets = repo.getAllBudgets()

    private val _defaultScope = repo.getPrefName(PreferenceConfig.BudgetScope.keyValue)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    //val transMonthList = mutableStateOf(ExpTrans())

    val state = combine(
        _state,
        //_budgets,
        _defaultScope
    ) { state,
        //budgets,
        defaultScope ->

        var newScope: String = state.scope

        if (defaultScope != null) {
            if (defaultScope != "") {
                newScope = defaultScope
                updateBudget(defaultScope)
            }
        }

        state.copy(
            //budgets = budgets,
            name = state.name,
            balance = state.balance,
            type = state.type,
            scope = newScope,
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

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val transMonthList = searchQuery.flatMapLatest { query ->
//        getCurrentMonth(query + 1, searchYear.value)
//    }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            emptyList()
//        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = combine(searchQuery,searchYear,state
        //_state.value.scope
    ) { month, year,state
        //newScope
        ->
        Pair(month, year,
            //newScope
        )
    }.flatMapLatest {
        getCurrentMonth(it.first + 1, it.second,  state.value.scope)
    }.stateIn(
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
                    selectedMonth = 0,
                    selectedYear = searchYear.value+1
                )
            }
            savedStateHandle[SEARCH_QUERY] = 0
            savedStateHandle[SEARCH_YEAR] = searchYear.value+1
        } else {
            _state.update {
                it.copy(
                    selectedMonth = 11,
                    selectedYear = searchYear.value-1
                )
            }
            savedStateHandle[SEARCH_QUERY] = 11
            savedStateHandle[SEARCH_YEAR] = searchYear.value-1
        }
    }

    fun onScopeChange(newScope: String) {
        _state.update {
            it.copy(
                scope = newScope
            )
        }
        viewModelScope.launch {
            repo.updatePref(PreferenceConfig.BudgetScope.keyValue, newScope,)
        }
        updateBudget(newScope)
    }

    fun updateBudget(newScope: String) {
        viewModelScope.launch {
            var budList = mutableListOf<budgets>()
            var days = getDaysInMonth(state.value.selectedYear, state.value.selectedMonth)
            val budgets2 = repo.getAllBudgets()

            budgets2.collectLatest {
                it.forEach {
                    var bud = budgets(
                        budgetid = it.budgetid,
                        name = it.name,
                        balance =
                            if (it.scope == BudgetScope.Week.name && newScope == BudgetScope.Week.name)
                                it.balance
                            else if (it.scope == BudgetScope.Month.name && newScope == BudgetScope.Week.name)
                                it.balance
                            else if (it.scope == BudgetScope.Year.name && newScope == BudgetScope.Week.name)
                                it.balance / (365/7)
                            else if (it.scope == BudgetScope.Week.name && newScope == BudgetScope.Month.name)
                                it.balance * (days/7)
                            else if (it.scope == BudgetScope.Month.name && newScope == BudgetScope.Month.name)
                                it.balance
                            else if (it.scope == BudgetScope.Year.name && newScope == BudgetScope.Month.name)
                                it.balance / 12
                            else if (it.scope == BudgetScope.Week.name && newScope == BudgetScope.Year.name)
                                it.balance *  (365/7)
                            else if (it.scope == BudgetScope.Month.name && newScope == BudgetScope.Year.name)
                                it.balance * 12
                            else if (it.scope == BudgetScope.Year.name && newScope == BudgetScope.Year.name)
                                it.balance
                            else it.balance,
                        type = it.type,
                        scope = it.scope
                    )
                    budList.add(bud)
                }
                _state.update {
                    it.copy(
                        budgets = budList
                    )
                }
            }
        }
    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}

private const val SEARCH_QUERY = "searchQuery"
private const val SEARCH_YEAR = "searchYear"
//private const val SEARCH_SCOPE = "searchScope"