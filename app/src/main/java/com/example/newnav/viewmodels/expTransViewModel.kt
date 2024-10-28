package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.models.ExpTranState
import com.example.newnav.data.expDAO
import com.example.newnav.data.expTrans
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class expTransViewModel(
    private val dao: expDAO
): ViewModel() {

        private val _state = MutableStateFlow(ExpTranState())
        private val _exptrn = dao.getAllExpTrans()
        val state= combine(_state,_exptrn) { state, exptrn ->
            state.copy(
                expTrans = exptrn,
                amount = state.amount
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpTranState())


    fun onAmountUpdate(newAmount: Double) {
        _state.update { it.copy(
            amount = newAmount
        ) }

    }

    fun onSaveExpense() {
        val newExp = expTrans(
            id = 0,
            amount = _state.value.amount
        )
        viewModelScope.launch {
            dao.insertExpTran(newExp)
        }
    }
}