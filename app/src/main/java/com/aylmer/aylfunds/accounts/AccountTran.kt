package com.aylmer.aylfunds.accounts

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aylmer.aylfunds.R
import com.aylmer.aylfunds.designsys.component.AylOutlinedNumber
import com.aylmer.aylfunds.designsys.component.DropdownList
import com.aylmer.aylfunds.navigation.AylTopBar
import com.aylmer.aylfunds.viewmodels.AccountViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountTran(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
)
{
    val state = viewModel.state.collectAsState()
    var accTypeList = state.value.accTypeList

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.onBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAccountAdd()
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
                titleRes = "Account",
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
                itemList = accTypeList,
                onTypeChange = { viewModel.onAccTypeChange(it) },
                defaultSelectedItem = ""
            )


//            OutlinedTextField(
//                label = { Text(text = "Balance") },
//                value = state.value.balance.toString(),
//                modifier = Modifier.fillMaxWidth(),
//                onValueChange = { viewModel.onBalanceChange(it) },
//                maxLines = 1,
//                colors = textFieldColors,
//            )

            AylOutlinedNumber(
                label = "Balance",
                value = state.value.tmpBalance,
                onValueChange = { viewModel.onBalanceChange(it) },
                modifier = Modifier.fillMaxWidth(),
            )

        }
    }
}