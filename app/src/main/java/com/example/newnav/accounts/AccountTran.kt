package com.example.newnav.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import com.example.newnav.R
import com.example.newnav.designsys.component.DropdownList
import com.example.newnav.viewmodels.AccountViewModel



@Composable
fun AccountTran(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
)
{
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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

            DropdownList(viewModel)

            OutlinedTextField(
                label = { Text(text = "Balance") },
                value = state.value.balance.toString(),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { viewModel.onBalanceChange(it) },
                maxLines = 1,
                colors = textFieldColors,
            )

        }
    }
}