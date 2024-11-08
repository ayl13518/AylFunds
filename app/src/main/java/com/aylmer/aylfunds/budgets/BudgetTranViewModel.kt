package com.aylmer.aylfunds.budgets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.budgets
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.BudgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BudgetTranViewModel @Inject constructor(
    private val repo: MainRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val budgeId: Long? = savedStateHandle["budgetID"]
    var newBudget :Long = 0

    private val _state = MutableStateFlow(BudgetState())

    val state = _state.asStateFlow()

    init {
        if (budgeId != null && budgeId != 0L) {
            newBudget = budgeId
            val curBud = repo.getBudgetById(newBudget)

            viewModelScope.launch {
                curBud.collectLatest { cur ->
                    _state.update { st ->
                        st.copy(
                            name = cur.name,
                            balance = cur.balance,
                            type = cur.type,
                            scope = cur.scope,
                            tmpBalance = cur.balance.toString()
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(newBudgetName: String) {
        _state.update {
            it.copy(
                name = newBudgetName
            )
        }
    }

    fun onTypeChange(newType: String) {
        _state.update {
            it.copy(
                type = newType
            )
        }
    }

    fun onScopeChange(newScope: String) {
        _state.update {
            it.copy(
                scope = newScope
            )
        }
    }

    fun onBalanceChange(newBalance: String) {
        _state.update {
            it.copy(
                tmpBalance = newBalance,
                balance = newBalance.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun onBudgetAdd() {
        val newAccount = budgets (
            name = _state.value.name,
            type = _state.value.type,
            balance = _state.value.balance,
            scope = _state.value.scope,
            budgetid = newBudget
        )
        viewModelScope.launch {
            repo.upsertBudget(newAccount)
        }
    }

}