package com.example.newnav.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.di.MainRepository
import com.example.newnav.models.ExpTranState
import com.example.newnav.models.PreferenceConfig
import com.example.newnav.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _state = MutableStateFlow(UserData())

    private val _prefAll = mainRepo.getAllPreference()
    private val _accountList = mainRepo.getAllAccountName()

    private val defaultAccount = mainRepo.getPrefName(PreferenceConfig.DefaultAccount.keyValue)


    val searchCategory = savedStateHandle.getStateFlow(key = SEARCH_Category, initialValue = "Expense")

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryFiltered = searchCategory.flatMapLatest { query ->
        mainRepo.getCategoryByType(query)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val state= combine(_state,_prefAll,_accountList,defaultAccount
    ) {  state, preference,accountList, defaultAccount ->
        state.copy(
              accountList = accountList,
              defaultAccount = defaultAccount,
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserData())

}

private const val SEARCH_Category = "Expense"