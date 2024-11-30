package com.aylmer.aylfunds.home


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.PrevMonth
import com.aylmer.aylfunds.di.MainRepository
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
class HomeScreenViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _accounts = mainRepo.getAllAccounts()
    private val _budgets = mainRepo.getAllBudgets()
    //private val _prevMonth = mainRepo.getPrevMonth()

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
        transMonthList,
       // _prevMonth,
    ) { state,
        accounts,
        budgets,
        transMonthList,
        //prevMonth
        ->
        state.copy(
            accounts = accounts,
            budgets = budgets,
            expTrans = transMonthList,
            //prevMonth = prevMonth
        )
        //formatPrevMonths()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState()
    )

    fun formatPrevMonths() {
        viewModelScope.launch{
            var budList = mutableListOf<PrevMonth>()
            val budgets2 = mainRepo.getPrevMonth()
            var count = 0
            var month =0
            var year = 0

            budgets2.collectLatest {
                it.forEach {
                    var bud = it
                    budList.add(bud)
                    count++
                    month = if (bud.month>month) bud.month else month
                    year = if (bud.year>year) bud.year else year
                }
                if (count < 3) {
                    repeat(3 - count) {
                        month++
                        if (month > 12) {
                            month = 1
                            year++
                        }

                        var bud = PrevMonth(
                           totalExpense = 0.0,
                            totalIncome = 0.0,
                            month = month,
                            year = year
                        )
                        budList.add(bud)
                    }
                }



                _state.update {
                    it.copy(
                         prevMonth =  budList.sortedWith(compareBy({it.year},{it.month}))
                    )
                }
            }
        }
    }


}

private const val SEARCH_QUERY = "searchQuery"