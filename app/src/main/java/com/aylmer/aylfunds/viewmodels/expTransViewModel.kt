package com.aylmer.aylfunds.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.models.ExpTranState
import com.aylmer.aylfunds.data.expDAO
import com.aylmer.aylfunds.data.expTrans
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.transactions.GetCurrentTransactions
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
class ExpTransViewModel @Inject constructor(
    private val dao: expDAO,
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    getcurrentMonth: GetCurrentTransactions,
    ): ViewModel() {

    private val _state = MutableStateFlow(ExpTranState())

    private val _exptrn = dao.getAllExpTrans()
    private val _categoryList = mainRepo.getAllCategory()
    private val _accountList = mainRepo.getAllAccountName()
    private val _typeList = _state.value.typeList

    val defaultAccount = mainRepo.getPrefName(PreferenceConfig.DefaultAccount.keyValue)
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
           initialValue =  "")

    val defaultCategory = mainRepo.getPrefName(PreferenceConfig.ExpenseCategory.keyValue)
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue =  "")

    val searchCategory = savedStateHandle.getStateFlow(key = SEARCH_Category
        , initialValue = TransactionType.Expense.name)

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryFiltered = searchCategory.flatMapLatest { query ->
        mainRepo.getCategoryByType(query)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = _state.value.selectedMonth)

    @OptIn(ExperimentalCoroutinesApi::class)
    val transMonthList = searchQuery.flatMapLatest { query ->
        getcurrentMonth(query + 1)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())


    val state= combine(_state,_exptrn,_categoryList,_accountList,
        ) { state, exptrn, categoryList, accountList ->
        state.copy(
                expTrans = exptrn,
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
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ExpTranState())

    fun onAmountUpdate(newAmount: String) {
        _state.update { it.copy(
            tmpAmount = newAmount,
            amount = newAmount.toDoubleOrNull() ?: 0.0
        ) }
    }

    fun onDateUpdate(newDate: String) {
        _state.update { it.copy(
            dateTrans = newDate
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

    fun onTranTypeUpdate(newTranType: String) {
        _state.update { it.copy(
            tranType = newTranType,
            selectedType = _typeList.indexOf(newTranType)
        ) }
        savedStateHandle[SEARCH_Category] = newTranType
    }

    fun onNoteUpdate(newNote: String) {
        _state.update { it.copy(
            note = newNote
        ) }
    }

    fun onSwipe(newMonth: Int) {
        if(newMonth >=0 && newMonth <= 11 ) {
            _state.update {
                it.copy(
                    selectedMonth = newMonth
                )
            }
            savedStateHandle[SEARCH_QUERY] = newMonth
        }
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

        val newExp = expTrans(
            id = 0,
            amount = _state.value.amount
            , dateTrans = convertDateForDB(_state.value.dateTrans)
            , budName = _state.value.budName
            , accName = _state.value.accName
            , tranType = if( _state.value.tranType == "") "Expense" else _state.value.tranType
            , note = _state.value.note
        )
        viewModelScope.launch {
            mainRepo.updateAccountBalance(newExp)
            mainRepo.updatePref(newExp)
        }
    }
}

private const val SEARCH_QUERY = "searchQuery"
private const val SEARCH_Category = "Expense"