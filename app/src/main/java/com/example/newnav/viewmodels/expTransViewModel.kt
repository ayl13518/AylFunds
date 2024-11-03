package com.example.newnav.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnav.models.ExpTranState
import com.example.newnav.data.expDAO
import com.example.newnav.data.expTrans
import com.example.newnav.di.MainRepository
import com.example.newnav.transactions.GetCurrentTransactions
import com.example.newnav.utils.convertDateForDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.reduce
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

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = _state.value.selectedMonth)

    //private val _exptrn  = mainRepo.getExpByMonth(_state.value.selectedMonth + 1)

//    val transMonthList = mainRepo.getExpByMonth(_state.value.selectedMonth + 1)
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = _state.value.expTrans,
//            )

//    val transMonthList = getcurrentMonth(_state.value.selectedMonth + 1)

        @OptIn(ExperimentalCoroutinesApi::class)
        val transMonthList = searchQuery.flatMapLatest { query ->
            getcurrentMonth(query + 1)
        }


    val state= combine(_state,_exptrn,_categoryList,_accountList
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
//            viewModelScope.launch {
//                transMonthList.collect({  mainRepo.getExpByMonth(_state.value.selectedMonth + 1)})

                //transMonthList.collectLatest {  {  mainRepo.getExpByMonth(_state.value.selectedMonth + 1)} }
//                }
        }
    }


    fun onSaveExpense() {
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
        }
    }
}

private const val SEARCH_QUERY = "searchQuery"