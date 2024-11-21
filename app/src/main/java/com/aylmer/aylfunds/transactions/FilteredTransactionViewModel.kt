package com.aylmer.aylfunds.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.ExpTranState
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


private const val SEARCH_QUERY = "id"
private const val SEARCH_YEAR = "searchYear"
private const val SEARCH_MONTH = "searchMonth"
private const val SEARCH_TYPE = "type"

@HiltViewModel
class FilteredTransactionViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
    ): ViewModel() {

    private val _state = MutableStateFlow(ExpTranState())
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()

    private val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = 0L)
    private val searchType = savedStateHandle.getStateFlow(key = SEARCH_TYPE, initialValue = "")

    private val searchYear = savedStateHandle.getStateFlow(key = SEARCH_YEAR, initialValue = Calendar.getInstance().get(Calendar.YEAR))
    private val searchMonth = savedStateHandle.getStateFlow(key = SEARCH_MONTH, initialValue = selectedMonth)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = searchMonth.flatMapLatest { query ->
        getCurrentMonth(id= searchQuery.value , searchMonth = query +1, searchYear = searchYear.value, type = searchType.value)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val transferMonthList = searchMonth.flatMapLatest { query ->
       mainRepo.getTransferByType(id= searchQuery.value , month = query +1, year = searchYear.value, type = searchType.value)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val accountBalance = mainRepo.getAllAccounts()
        .stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val state= combine(
        _state,
        _categoryList,
        _accountList,
        ) { state,
            categoryList,
            accountList ->
        state.copy(
                amount = state.amount,
                dateTrans = state.dateTrans
                , accName = state.accName
                , budName = state.budName
                , tranType = state.tranType
                , note = state.note
                , categoryList = categoryList
            , accountList = accountList
            , tmpAmount = state.tmpAmount
            , selectedType = state.selectedType
            ,selectedMonth = state.selectedMonth
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ExpTranState())


    fun onSwipe(newMonth: Int) {
        if(newMonth >=0 && newMonth <= 11 ) {
            _state.update {
                it.copy(
                    selectedMonth = newMonth
                )
            }
            savedStateHandle[SEARCH_MONTH] = newMonth
        }
        else if(newMonth >= 12 ) {
            _state.update {
                it.copy(
                    selectedMonth = 0
                )
            }
            savedStateHandle[SEARCH_MONTH] = 0
            savedStateHandle[SEARCH_YEAR] = searchYear.value+1
        }
        else {
            _state.update {
                it.copy(
                    selectedMonth = 11
                )
            }
            savedStateHandle[SEARCH_MONTH] = 11
            savedStateHandle[SEARCH_YEAR] = searchYear.value-1
        }
    }


}

