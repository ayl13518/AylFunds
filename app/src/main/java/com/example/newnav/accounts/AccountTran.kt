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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.R
import com.example.newnav.viewmodels.AccountViewModel


@Composable
fun AccountTran(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = viewModel()
)
{
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //viewModel.onSaveExpense()
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
//                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                //cursorColor = MaterialTheme.colorScheme.colors.secondary.copy(alpha = ContentAlpha.high)
            )
        }
    }
}