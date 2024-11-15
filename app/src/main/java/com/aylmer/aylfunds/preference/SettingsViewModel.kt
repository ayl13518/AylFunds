package com.aylmer.aylfunds.preference

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.UserData
import com.aylmer.aylfunds.workers.BackUpWorker2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aylmer.aylfunds.workers.ExportCSV


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _state = MutableStateFlow(UserData())

    private val _prefAll = mainRepo.getAllPreference()
    private val _accountList = mainRepo.getAllAccountName()

    private val _defaultAccount = mainRepo.getPrefName(PreferenceConfig.DefaultAccount.keyValue)

    val searchCategory = savedStateHandle.getStateFlow(key = SEARCH_Category, initialValue = "Expense")

    val files: Array<String> = context.fileList()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryFiltered = searchCategory.flatMapLatest { query ->
        mainRepo.getCategoryByType(query)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    val state= combine(_state,_prefAll,_accountList,_defaultAccount
    ) {  state, preference,accountList, defaultAccount ->
        state.copy(
              accountList = accountList,
              defaultAccount = defaultAccount,
//            useDarkTheme = preference
//                .find { it.key==PreferenceConfig.UseDarkTheme.keyValue}
//                ,
            useDarkTheme = preference
                .find { item -> item.key.toString()
                    .takeIf { it==PreferenceConfig.UseDarkTheme.keyValue } != null }?.name.toString(),
            restoreFile = state.restoreFile
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserData())

    fun onUpdateDarkTheme(newTheme: String) {
        _state.update { it.copy(
            useDarkTheme = newTheme
        ) }
        viewModelScope.launch {
            mainRepo.updatePref(keyValue = PreferenceConfig.UseDarkTheme.keyValue, name = newTheme)
        }
    }

    fun onUpdateDefaultAccount(newAccount: String) {
        _state.update {
            it.copy(
                defaultAccount = newAccount
            )
        }
        viewModelScope.launch {
            mainRepo.updatePref(keyValue = PreferenceConfig.DefaultAccount.keyValue, name = newAccount)
        }
    }

    fun onRestoreFileChange(newFileName: String) {
        _state.update {
            it.copy(
                restoreFile = newFileName
            )
        }
    }

    fun onBackup() {
//        val backupWorker: BackUpWorker2 = BackUpWorker2(context)
//        backupWorker.doBackUp()

        val backupWorker: ExportCSV = ExportCSV(context, mainRepo)
        backupWorker.checkDir()

        viewModelScope.launch {
            backupWorker.doExportCSV()
        }
    }

    fun onLoadBackUp(){
        if(_state.value.restoreFile == "") return

        //val backupWorker: BackUpWorker2 = BackUpWorker2(context)
        //backupWorker.doRestore(_state.value.restoreFile)
        val backupWorker: ExportCSV = ExportCSV(context, mainRepo)
        viewModelScope.launch {
            backupWorker.doRestoreCSV()
        }

    }


}

private const val SEARCH_Category = "Expense"