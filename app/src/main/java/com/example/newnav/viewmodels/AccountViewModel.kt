package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.accDAO
import com.example.newnav.data.accounts
import com.example.newnav.di.MainRepository
import com.example.newnav.models.AccState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repo: MainRepository

): ViewModel() {
    //TODO: Add StateFlow(s)

    private val _state = MutableStateFlow(AccState())
    private val _accounts = repo.getAllAccounts()
    val state =  combine(
        _state,
        _accounts
    ) { state, accounts ->
        state.copy(
            accounts = accounts,
            name = state.name,
            accType = state.accType,
            balance = state.balance,
            description = state.description,
            tmpBalance = state.tmpBalance
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AccState()
    )
    //TODO: Add Event(s)
    fun onNameChange(newAccountName: String) {
        _state.update {
            it.copy(
                name = newAccountName
            )
        }
    }

    fun onAccTypeChange(newAccType: String) {
        _state.update {
            it.copy(
                accType = newAccType
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

    fun onAccountAdd() {
        val newAccount = accounts (
            name = _state.value.name,
            accType = _state.value.accType,
            balance = _state.value.balance,
            description = _state.value.description
        )
        viewModelScope.launch {
            repo.insertAccount(newAccount)
        }

    }
}