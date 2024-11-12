package com.aylmer.aylfunds.scheduling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.ScheduleState
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.utils.convertDateForDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AddSchedViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val id: Long? = savedStateHandle["id"]
    private val tranType: String? = savedStateHandle["tranType"]
    var newTran :Long = 0

    private val _state = MutableStateFlow(ScheduleState())
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()

    val defaultAccount = mainRepo.getPrefName(PreferenceConfig.DefaultAccount.keyValue)
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue =  "")

    val defaultCategory = mainRepo.getPrefName(PreferenceConfig.ExpenseCategory.keyValue)
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue =  "")

    private val searchCategory = savedStateHandle.getStateFlow(key = SEARCH_Category
        , initialValue = TransactionType.Expense.name)

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryFiltered = searchCategory.flatMapLatest { query ->
        mainRepo.getCategoryByType(query)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val state  = combine(
        _state,
        _categoryList,
        _accountList,
    ) { state,
        categoryList,
        accountList ->
        state.copy(
            amount = state.amount,
            dateTrans = state.dateTrans
            , accName = state.accName
            , budName = state.budName
            , tranType = state.tranType
            , note = state.note
            , categoryList = categoryList
            , accountList = accountList
            , tmpAmount = state.tmpAmount
            , selectedType = state.selectedType
            ,selectedMonth = state.selectedMonth
            ,id = state.id
            ,schedule = state.schedule
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ScheduleState())

    fun onAmountUpdate(newAmount: String) {
        _state.update { it.copy(
            tmpAmount = newAmount,
            amount = newAmount.toDoubleOrNull() ?: 0.0
        ) }
    }

    fun onBudgetUpdate(newBud: String) {
        _state.update { it.copy(
            budName = newBud
        ) }
    }

    fun onAccUpdate(newAcc: String) {
        _state.update { it.copy(
            accName = newAcc
        ) }
    }

    fun onAccUpdateTo(newAcc: String) {
        _state.update { it.copy(
            accNameTo = newAcc
        ) }
    }

    fun onTranTypeUpdate(newTranType: String) {
        _state.update { it.copy(
            tranType = newTranType,
            selectedType =  TransactionType.valueOf(newTranType).ordinal ,

        ) }
        savedStateHandle[SEARCH_Category] = newTranType
    }

    fun onNoteUpdate(newNote: String) {
        _state.update { it.copy(
            note = newNote
        ) }
    }

    fun onDateUpdate(newDate: String) {
        _state.update { it.copy(
            dateTrans = newDate
        ) }
    }

    fun onPeriodUpdate(newPeriod: String) {
        _state.update { it.copy(
            period = newPeriod
        ) }
    }

    fun onSaveExpense() {
        if (_state.value.accName=="" && defaultAccount.value != "") {
            _state.update { it.copy(
                accName = defaultAccount.value
            ) }
        }

        if (_state.value.budName=="" && defaultCategory.value != "") {
            _state.update { it.copy(
                budName = defaultCategory.value
            ) }
        }

        if (_state.value.tranType == "Transfer" ) {
            //onSaveTransfer()
        }
        else {
            val newExp = Schedule(
                id = _state.value.id,
                amount = _state.value.amount,
                dateTrans = convertDateForDB(_state.value.dateTrans),
                budName = _state.value.budName,
                accName = _state.value.accName,
                tranType = if (_state.value.tranType == "") "Expense" else _state.value.tranType,
                note = _state.value.note,
                period = _state.value.period
            )
            viewModelScope.launch {
                //mainRepo.updateAccountBalance(newExp)
                mainRepo.upsertSchedule(newExp)
                //mainRepo.updatePref(newExp)
            }
        }
    }

}

private const val SEARCH_Category = "Expense"