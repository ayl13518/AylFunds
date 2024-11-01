package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.models.ExpTranState
import com.example.newnav.data.expDAO
import com.example.newnav.data.expTrans
import com.example.newnav.di.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExpTransViewModel @Inject constructor(
    private val dao: expDAO,
    private val mainRepo: MainRepository
): ViewModel() {

    private val _state = MutableStateFlow(ExpTranState())
    private val _exptrn = dao.getAllExpTrans()
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()

    val state= combine(_state,_exptrn,_categoryList,_accountList
        ) { state, exptrn, categoryList, accountList ->
        state.copy(
                expTrans = exptrn,
                amount = state.amount,
                dateTrans = state.dateTrans
                , accName = state.accName
                , budName = state.budName
                , tranType = state.tranType
                , note = state.note
                , categoryList = categoryList
            , accountList = accountList
            , tmpAmount = state.tmpAmount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpTranState())

    fun onAmountUpdate(newAmount: String) {
        _state.update { it.copy(
            tmpAmount = newAmount,
            amount = newAmount.toDoubleOrNull() ?: 0.0
        ) }
    }

    fun onDateUpdate(newDate: String) {
        _state.update { it.copy(
            dateTrans = newDate
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

    fun onTranTypeUpdate(newTranType: String) {
        _state.update { it.copy(
            tranType = newTranType
        ) }
    }

    fun onSaveExpense() {
        val newExp = expTrans(
            id = 0,
            amount = _state.value.amount
            , dateTrans = _state.value.dateTrans
            , budName = _state.value.budName
            , accName = _state.value.accName
            , tranType = _state.value.tranType
            , note = _state.value.note
        )
        viewModelScope.launch {
            mainRepo.updateAccountBalance(newExp)
        }
    }
}