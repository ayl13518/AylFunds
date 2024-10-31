package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.budgets
import com.example.newnav.di.MainRepository
import com.example.newnav.models.BudgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repo: MainRepository

): ViewModel() {
    //TODO: Add StateFlow(s)

    private val _state = MutableStateFlow(BudgetState())
    private val _accounts = repo.getAllBudgets()
    val state =  combine(
        _state,
        _accounts
    ) { state, accounts ->
        state.copy(
            budgets = accounts,
            name = state.name,
            balance = state.balance,
            type = state.type,
            scope = state.scope,
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetState()
    )
    //TODO: Add Event(s)
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
                balance = newBalance.toDouble()
            )
        }
    }

    fun onBudgetAdd() {
        val newAccount = budgets (
            name = _state.value.name,
            type = _state.value.type,
            balance = _state.value.balance,
            scope = _state.value.scope,
        )
        viewModelScope.launch {
            repo.insertBudget(newAccount)
        }

    }
}