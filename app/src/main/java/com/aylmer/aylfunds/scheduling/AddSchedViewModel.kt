package com.aylmer.aylfunds.scheduling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.ExpTranState
import com.aylmer.aylfunds.models.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject



@HiltViewModel
class AddSchedViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val tranId: Long? = savedStateHandle["tranId"]
    private val tranType: String? = savedStateHandle["tranType"]
    var newTran :Long = 0

    private val _state = MutableStateFlow(ExpTranState())
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()

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
            ,tranId = state.tranId
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ExpTranState())

    fun onAmountUpdate(newAmount: String) {
        _state.update { it.copy(
            tmpAmount = newAmount,
            amount = newAmount.toDoubleOrNull() ?: 0.0
        ) }
    }

    fun onBudgetUpdate(newBud: String) {
        _state.update { it.copy(
            budName = newBud
        ) }
    }

    fun onAccUpdate(newAcc: String) {
        _state.update { it.copy(
            accName = newAcc
        ) }
    }

    fun onAccUpdateTo(newAcc: String) {
        _state.update { it.copy(
            accNameTo = newAcc
        ) }
    }

    fun onTranTypeUpdate(newTranType: String) {
        _state.update { it.copy(
            tranType = newTranType,
            selectedType =  TransactionType.valueOf(newTranType).ordinal ,

        ) }
        //savedStateHandle[SEARCH_Category] = newTranType
    }

    fun onNoteUpdate(newNote: String) {
        _state.update { it.copy(
            note = newNote
        ) }
    }

    fun onDateUpdate(newDate: String) {
        _state.update { it.copy(
            dateTrans = newDate
        ) }
    }

}