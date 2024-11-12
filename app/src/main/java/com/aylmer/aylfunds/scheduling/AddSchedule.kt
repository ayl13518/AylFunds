package com.aylmer.aylfunds.scheduling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.R
import com.aylmer.aylfunds.designsys.component.AylOutlinedNumber
import com.aylmer.aylfunds.designsys.component.AylOutlinedTextField
import com.aylmer.aylfunds.designsys.component.AylTab
import com.aylmer.aylfunds.designsys.component.AylTabRow
import com.aylmer.aylfunds.designsys.component.DropdownList
import com.aylmer.aylfunds.designsys.component.OutlinedDatesField
import com.aylmer.aylfunds.models.PeriodType
import com.aylmer.aylfunds.models.TransactionType
import com.aylmer.aylfunds.navigation.AylTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSchedule(
    navController: NavHostController = rememberNavController(),
    viewModel: AddSchedViewModel = hiltViewModel(),
    id: Long=0,
    tranType: String = TransactionType.Expense.name
)
{

    val state by viewModel.state.collectAsStateWithLifecycle()
    val categoryList by viewModel.categoryFiltered.collectAsState(emptyList())
    var accountList = state.accountList

    val defaultAccount by viewModel.defaultAccount.collectAsStateWithLifecycle()
    val defaultCategory by viewModel.defaultCategory.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onSaveExpense()
                    navController.popBackStack()
                },
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.activity_horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {

            AylTopBar(
                titleRes = "Schedule",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = { navController.popBackStack() },
                actionIcon = Icons.Default.DeleteForever,
                actionIconContentDescription = "Delete Forever",
                onActionClick = {
                    //viewModel.onDeleteTransaction()
                    navController.popBackStack()
                }
            )

            AylTabRow(selectedTabIndex = 0) {
                TransactionType.entries.forEach {  title ->
                    //var index : Int
                    AylTab(
                        selected =  false,
                        onClick = { viewModel.onTranTypeUpdate(title.name)
                                  },
                        text = { Text(text = title.name) },
                    )
                }
            }

            AylOutlinedNumber(
                label = "Amount",
                value = state.tmpAmount,
                onValueChange = { viewModel.onAmountUpdate(it)  },
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.selectedType != 2) {
                DropdownList(
                    label = "Category",
                    itemList = categoryList,
                    onTypeChange = { viewModel.onBudgetUpdate(it) },
                    modifier = Modifier.fillMaxWidth(),
                    defaultSelectedItem = defaultCategory
                )
            }

            DropdownList(
                label = "Account",
                itemList = accountList,
                onTypeChange = { viewModel.onAccUpdate(it) },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = if(state.accName == "") defaultAccount else state.accName,
            )

            if (state.selectedType == 2) {
                DropdownList(
                    label = "Transfer To",
                    itemList = accountList,
                    onTypeChange = { viewModel.onAccUpdateTo(it) },
                    modifier = Modifier.fillMaxWidth(),
                    defaultSelectedItem = if(state.accNameTo == "") defaultAccount else state.accNameTo,
                )
            }

//            DateTransaction(
//                textFieldColors,
//                viewModel
//            )

            OutlinedDatesField(
                onValueChange = { viewModel.onDateUpdate(it) } ,
                defaultDate = state.dateTrans
            )

            AylOutlinedTextField(
                label = "Notes",
                value = state.note,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { viewModel.onNoteUpdate(it) },
                maxLines = 3,
            )

            DropdownList(
                label = "Schedule",
                itemList = PeriodType.entries.map { it.name },
                onTypeChange = {viewModel.onPeriodUpdate(it)  },
                modifier = Modifier.fillMaxWidth(),
                defaultSelectedItem = state.period,
            )

        }
    }
}