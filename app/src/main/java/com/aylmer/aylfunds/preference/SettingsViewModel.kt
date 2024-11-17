package com.aylmer.aylfunds.preference

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aylmer.aylfunds.di.MainRepository
import com.aylmer.aylfunds.models.PreferenceConfig
import com.aylmer.aylfunds.models.UserData
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
import com.aylmer.aylfunds.backuprestore.ExportCSV
import com.aylmer.aylfunds.backuprestore.RestoreCSV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepo: MainRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _state = MutableStateFlow(UserData())

    private val _prefAll = mainRepo.getAllPreference().flowOn(Dispatchers.IO)
    private val _accountList = mainRepo.getAllAccountName().flowOn(Dispatchers.IO)

    private val _defaultAccount = mainRepo.getPrefName(PreferenceConfig.DefaultAccount.keyValue)

    val searchCategory =
        savedStateHandle.getStateFlow(key = SEARCH_Category, initialValue = "Expense")

    val files: Array<String> = context.fileList()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryFiltered = searchCategory.flatMapLatest { query ->
        mainRepo.getCategoryByType(query)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val state = combine(
        _state, _prefAll, _accountList, _defaultAccount
    ) { state, preference, accountList, defaultAccount ->
        state.copy(
            accountList = accountList,
            defaultAccount = defaultAccount,
            useDarkTheme = preference
                .find { item ->
                    item.key.toString()
                        .takeIf { it == PreferenceConfig.UseDarkTheme.keyValue } != null
                }?.name.toString(),
            restoreFile = state.restoreFile
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserData()
    )

    fun onUpdateDarkTheme(newTheme: String) {
        _state.update {
            it.copy(
                useDarkTheme = newTheme
            )
        }
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
            mainRepo.updatePref(
                keyValue = PreferenceConfig.DefaultAccount.keyValue,
                name = newAccount
            )
        }
    }

    fun onRestoreFileChange(newFileName: String) {
        _state.update {
            it.copy(
                restoreFile = newFileName
            )
        }
    }

    fun onBackup(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val backupWorker: ExportCSV = ExportCSV(context, mainRepo)
        backupWorker.checkDir()

        viewModelScope.launch {
            withContext(Dispatchers.IO) { backupWorker.doExportAccounts(scope, snackBarHostState) }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { backupWorker.doExportBudgets(scope,snackBarHostState) }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { backupWorker.doExportTransactions(scope, snackBarHostState) }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { backupWorker.doExportTransfer(scope, snackBarHostState) }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) { backupWorker.doExportSchedules(scope, snackBarHostState) }
        }

       // viewModelScope.launch{
//            scope.launch(
//                snackbarHostState.showSnackbar("Done Backup")
//            )
       // }

    }

    fun onLoadBackUp(scope: CoroutineScope, snackBarHostState: SnackbarHostState) {

        val backupWorker: RestoreCSV = RestoreCSV(context, mainRepo)

        viewModelScope.launch {
            backupWorker.doRestoreAccounts( scope, snackBarHostState)
        }

        viewModelScope.launch {
            backupWorker.doRestoreBudgets(scope, snackBarHostState)
        }

        viewModelScope.launch {
            backupWorker.doRestoreTransactions(scope, snackBarHostState)
        }

        viewModelScope.launch {
            backupWorker.doRestoreTransfers(scope, snackBarHostState)
        }

        viewModelScope.launch {
            backupWorker.doRestoreSchedules(scope, snackBarHostState)
        }

    }


}

private const val SEARCH_Category = "Expense"