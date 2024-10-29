package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.accDAO
import com.example.newnav.models.AccState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AccountViewModel (
    private val dao: accDAO

): ViewModel() {
    //TODO: Add StateFlow(s)
    private val _state = MutableStateFlow(AccState())
    private val _accounts = dao.getAllAccounts()
    val state =  combine(
        _state,
        _accounts
    ) { state, accounts ->
        state.copy(
            accounts = accounts,
            name = state.name,
            accType = state.accType,
            balance = state.balance,
            description = state.description
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AccState()
    )
    //TODO: Add Event(s)

}