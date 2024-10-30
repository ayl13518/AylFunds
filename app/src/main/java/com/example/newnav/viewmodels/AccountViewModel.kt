package com.example.newnav.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.data.accDAO
import com.example.newnav.di.MainRepository
import com.example.newnav.models.AccState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
            description = state.description
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AccState()
    )
    //TODO: Add Event(s)

}