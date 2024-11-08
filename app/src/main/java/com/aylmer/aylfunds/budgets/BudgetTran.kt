package com.aylmer.aylfunds.budgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.R
import com.aylmer.aylfunds.designsys.component.AylOutlinedNumber
import com.aylmer.aylfunds.designsys.component.DropdownList
import com.aylmer.aylfunds.models.BudgetState
import com.aylmer.aylfunds.navigation.AylTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTran(
    navController: NavHostController = rememberNavController(),
    viewModel: BudgetTranViewModel = hiltViewModel(),
    budgetId: Long=0
)
{
    val state = viewModel.state.collectAsStateWithLifecycle(BudgetState())
    var typeList = state.value.budTypeList
    var scopeList = state.value.budScopeList


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onBudgetAdd()
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
                titleRes = "Budget",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = {navController.popBackStack()},
                actionIcon = Icons.Default.MoreVert,
                actionIconContentDescription = "Action icon",
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            )

            OutlinedTextField(
                label = { Text(text = "Name") },
                value = state.value.name,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { viewModel.onNameChange(it) },
                maxLines = 1,
                colors = textFieldColors,
            )

            DropdownList(
                label = "Type",
                itemList = typeList,
                onTypeChange = { viewModel.onTypeChange(it) },
                defaultSelectedItem = state.value.type
            )


            AylOutlinedNumber(
                label = "Planned Amount",
                value = state.value.tmpBalance,
                onValueChange = { viewModel.onBalanceChange(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            DropdownList(
                label = "Scope",
                itemList = scopeList,
                onTypeChange = { viewModel.onScopeChange(it) },
                defaultSelectedItem = state.value.scope
            )

        }
    }
}