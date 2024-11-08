package com.example.newnav.budgets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.budgets
import com.example.newnav.di.MainRepository
import com.example.newnav.models.BudgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
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

    private val _State = MutableStateFlow(BudgetState())

    val state = _State.asStateFlow()

    init {
        if (budgeId != null && budgeId != 0L) {
            newBudget = budgeId
            val curBud = repo.getBudgetById(newBudget)

            viewModelScope.launch {
                curBud.collectLatest { cur ->
                    _State.update { st ->
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
        _State.update {
            it.copy(
                name = newBudgetName
            )
        }
    }

    fun onTypeChange(newType: String) {
        _State.update {
            it.copy(
                type = newType
            )
        }
    }

    fun onScopeChange(newScope: String) {
        _State.update {
            it.copy(
                scope = newScope
            )
        }
    }

    fun onBalanceChange(newBalance: String) {
        _State.update {
            it.copy(
                tmpBalance = newBalance,
                balance = newBalance.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun onBudgetAdd() {
        val newAccount = budgets (
            name = _State.value.name,
            type = _State.value.type,
            balance = _State.value.balance,
            scope = _State.value.scope,
            budgetid = newBudget
        )
        viewModelScope.launch {
            repo.upsertBudget(newAccount)
        }
    }

}