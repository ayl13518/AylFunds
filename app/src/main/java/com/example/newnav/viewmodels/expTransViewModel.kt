package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.models.ExpTranState
import com.example.newnav.data.expDAO
import com.example.newnav.data.expTrans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class expTransViewModel @Inject constructor(
    private val dao: expDAO
): ViewModel() {

        private val _state = MutableStateFlow(ExpTranState())
        private val _exptrn = dao.getAllExpTrans()
        val state= combine(_state,_exptrn) { state, exptrn ->
            state.copy(
                expTrans = exptrn,
                amount = state.amount,
                dateTrans = state.dateTrans
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpTranState())


    fun onAmountUpdate(newAmount: String) {
        _state.update { it.copy(
            amount = newAmount.toDouble()
        ) }

    }

    fun onDateUpdate(newDate: String) {
        _state.update { it.copy(
            dateTrans = newDate
        ) }
    }

    fun onSaveExpense() {
        val newExp = expTrans(
            id = 0,
            amount = _state.value.amount
            , dateTrans = _state.value.dateTrans
        )
        viewModelScope.launch {
            dao.insertExpTran(newExp)
        }
    }
}