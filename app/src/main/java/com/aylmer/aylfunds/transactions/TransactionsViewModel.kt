package com.aylmer.aylfunds.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.models.ExpTranState
import com.aylmer.aylfunds.di.MainRepository
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
class ExpTransViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getCurrentMonth: GetCurrentTransactions,
    ): ViewModel() {

    private val _state = MutableStateFlow(ExpTranState())
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()
    private val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = selectedMonth)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = searchQuery.flatMapLatest { query ->
        getCurrentMonth(query + 1)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val transferMonthList = searchQuery.flatMapLatest { query ->
       mainRepo.getTransferByMonth(query + 1)
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
            savedStateHandle[SEARCH_QUERY] = newMonth
        }
        else if(newMonth >= 12 ) {
            _state.update {
                it.copy(
                    selectedMonth = 0
                )
            }
            savedStateHandle[SEARCH_QUERY] = 0
        }
        else {
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