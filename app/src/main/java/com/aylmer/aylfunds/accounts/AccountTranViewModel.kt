package com.aylmer.aylfunds.accounts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.accounts
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.AccState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountTranViewModel @Inject constructor(
    private val repo: MainRepository,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val accountId: Long? = savedStateHandle["accountId"]
    var newAccountId :Long = 0

    private val _state = MutableStateFlow(AccState())
    val state = _state.asStateFlow()

    init {
        if (accountId != null && accountId != 0L) {
            newAccountId = accountId
            val curAccount = repo.getAccountById(newAccountId)

            viewModelScope.launch {
                curAccount.collectLatest { cur ->
                    _state.update { st ->
                        st.copy(
                            name = cur.name,
                            balance = cur.balance,
                            accType = cur.accType,
                            description = cur.description,
                            tmpBalance = cur.balance.toString()
                        )
                    }
                }
            }
        }
    }

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
            description = _state.value.description,
            id = newAccountId
        )
        viewModelScope.launch {
            repo.upsertAccount(newAccount)
        }

    }

}