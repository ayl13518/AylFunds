package com.aylmer.aylfunds.scheduling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.data.ExpTrans
import com.aylmer.aylfunds.data.Schedule
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.ComputeType
import com.aylmer.aylfunds.models.PeriodType
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.ScheduleState
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.utils.convertDateForDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.toDoubleOrNull


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

    private val accountBalance = mainRepo.getAllAccounts()
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList())

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
            , period = state.period
            , computeType = state.computeType
            , computePercent = state.computePercent
            , tmpPercent = state.tmpPercent
            , taxPercent = state.taxPercent
            ,tmpTaxPercent = state.taxPercent.toString()
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ScheduleState())

    init {
        if (tranType != null && tranType == TransactionType.Transfer.name)
            //refreshTransfer()
        else
            refresh()
    }

    fun refresh() {
        if (id != null && id != 0L) {
            newTran = id
            val curTran = mainRepo.getScheduleById(newTran)

            viewModelScope.launch {
                curTran.collectLatest { cur ->
                    //if (cur == null) return@collectLatest

                    _state.update { st ->
                        st.copy(
                            amount =  cur.amount,
                            dateTrans = cur.dateTrans
                            , accName = cur.accName
                            , budName = cur.budName
                            , tranType = cur.tranType
                            , note = cur.note
                            , tmpAmount = cur.amount.toString()
                            ,id = cur.id
                            ,selectedType = TransactionType.valueOf(cur.tranType).ordinal
                             ,period = cur.period
                            , computeType = cur.computeType
                            , computePercent = cur.computePercent
                            ,tmpPercent = cur.computePercent.toString()

                            , taxPercent = cur.taxPercent
                            ,tmpTaxPercent = cur.taxPercent.toString()
                        )
                    }
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

    fun onPercentUpdate(newPercent: String) {
        _state.update { it.copy(
            tmpPercent = newPercent,
            computePercent = newPercent.toDoubleOrNull() ?: 0.0
        ) }
        computeAmount()
    }

    fun onComputeUpdate(newCompute: String) {
        _state.update { it.copy(
            computeType = newCompute
        ) }
        computeAmount()
    }

    fun onTaxUpdate(newTax:String){
        _state.update { it.copy(
            tmpTaxPercent = newTax,
            taxPercent = newTax.toDoubleOrNull() ?: 0.0
        ) }
        computeAmount()
    }

    fun computeAmount()
    {
        var newAmount = _state.value.amount

        if(_state.value.computeType == ComputeType.Per_Annum.name){

//            var balance : Double = accountBalance.collect { it ->
//                it.find { it.name == _state.value.accName }?.balance
//            }
            var balance= accountBalance.value.find { it.name == _state.value.accName }?.balance

            if(_state.value.period == PeriodType.Daily.name && balance != null) {

                newAmount = (balance * (_state.value.computePercent / 100))/365
                newAmount = newAmount - (newAmount * (_state.value.taxPercent / 100))

                _state.update { it.copy(
                    tmpAmount = newAmount.toString(),
                    amount = newAmount
                ) }
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
                period = _state.value.period,
                computeType = _state.value.computeType,
                computePercent = _state.value.computePercent,
                taxPercent = _state.value.taxPercent
            )
            viewModelScope.launch {
                mainRepo.upsertSchedule(newExp)
            }
        }
    }

}

private const val SEARCH_Category = "Expense"