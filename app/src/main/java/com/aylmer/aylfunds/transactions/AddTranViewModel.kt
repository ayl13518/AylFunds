package com.aylmer.aylfunds.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.TransferTransactions
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.ExpTranState
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.utils.convertDateForDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTranViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val tranId: Long? = savedStateHandle["tranId"]
    private val tranType: String? = savedStateHandle["tranType"]
    var newTran :Long = 0

    private val _state = MutableStateFlow(ExpTranState())
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


    //val state = _state.asStateFlow()
    val state= combine(
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
            ,tranId = state.tranId
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ExpTranState())

    init {
        if (tranType != null && tranType == TransactionType.Transfer.name)
             refreshTransfer()
        else
            refresh()
    }

    fun refreshTransfer() {
        if (tranId != null && tranId != 0L) {
            newTran = tranId
            val curTran = mainRepo.getTransferTransactionById(newTran)

            viewModelScope.launch {
                curTran.collectLatest { cur ->
                    if (cur == null) return@collectLatest

                    _state.update { st ->
                        st.copy(
                            amount =  cur.amount,
                            dateTrans = cur.dateTrans
                            , accName = cur.accName
                            , budName = ""
                            , tranType = cur.tranType
                            , note = cur.note
                            , tmpAmount = cur.amount.toString()
                            ,tranId = cur.id
                            , accNameTo = cur.accNameTo
                            ,selectedType = _typeList.indexOf(cur.tranType)
                        )
                    }
                }
            }
        }
    }

    fun refresh() {
        if (tranId != null && tranId != 0L) {
            newTran = tranId
            val curTran = mainRepo.getTransactionById(newTran)

            viewModelScope.launch {
                curTran.collectLatest { cur ->
                    if (cur == null) return@collectLatest

                    _state.update { st ->
                        st.copy(
                            amount =  cur.amount,
                            dateTrans = cur.dateTrans
                            , accName = cur.accName
                            , budName = cur.budName
                            , tranType = cur.tranType
                            , note = cur.note
                            , tmpAmount = cur.amount.toString()
                            ,tranId = cur.id
                            ,selectedType = _typeList.indexOf(cur.tranType)
                        )
                    }
                    savedStateHandle[SEARCH_Category] = cur.tranType
                }
            }
        }
    }

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
        if (newTran ==0L) {
            _state.update {
                it.copy(
                    tranType = newTranType,
                    selectedType = _typeList.indexOf(newTranType)
                )
            }
            savedStateHandle[SEARCH_Category] = newTranType
        }

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

    fun onDeleteTransaction() {
        if (newTran !=0L) {
            viewModelScope.launch {
                if (_state.value.tranType != TransactionType.Transfer.name)
                    mainRepo.deleteTransaction(newTran)
                else
                    mainRepo.deleteTransferTransaction(newTran)
            }
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

        if (_state.value.tranType == "Transfer" ) {
            onSaveTransfer()
        }
        else {
            val newExp = ExpTrans(
                id = _state.value.tranId,
                amount = _state.value.amount,
                dateTrans = convertDateForDB(_state.value.dateTrans),
                budName = _state.value.budName,
                accName = _state.value.accName,
                tranType = if (_state.value.tranType == "") "Expense" else _state.value.tranType,
                note = _state.value.note
            )
            viewModelScope.launch {
                mainRepo.updateOldBalance(newExp.id)
                mainRepo.updateAccountBalance(newExp)
                mainRepo.updatePref(newExp)
            }
        }
    }

    fun onSaveTransfer() {
        val newExp = TransferTransactions(
            id = _state.value.tranId,
            amount = _state.value.amount
            , dateTrans = convertDateForDB(_state.value.dateTrans)
            , accName = _state.value.accName
            , accNameTo = _state.value.accNameTo
            , tranType = _state.value.tranType
            , note = _state.value.note
        )

        viewModelScope.launch {
            mainRepo.updateAccountBalance(newExp)

        }
    }

}

private const val SEARCH_Category = "Expense"