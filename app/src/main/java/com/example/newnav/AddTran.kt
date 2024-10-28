package com.example.newnav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.viewmodels.expTransViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddTranScreen(
    //onBack: () -> Unit,
    onAmtChange: (String) -> Unit,
    navController: NavHostController = rememberNavController(),
    viewModel: expTransViewModel = viewModel()
)
{
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onSaveExpense()
                    navController.navigate(ScreenHome)
                },
                ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    ){ modifier ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.activity_horizontal_margin))
                .verticalScroll(rememberScrollState())
        ) {
//            val textFieldColors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color.Transparent,
//                unfocusedBorderColor = Color.Transparent,
//                //cursorColor = MaterialTheme. .colors.secondary.copy(alpha = ContentAlpha.high)
//            )

           OutlinedTextField(
                label = { Text(text = "Amount") },
                value = state.amount.toString(),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAmtChange(it) },
                maxLines = 1,
                //colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = "category",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },

                maxLines = 1,
                //colors = textFieldColors
            )
            OutlinedTextField(
                value = "account",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
               // colors = textFieldColors
            )
            OutlinedTextField(
                label = { Text(text = "Date") },
                value = "date",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { },
                maxLines = 1,
               // colors = textFieldColors

            )
            OutlinedTextField(
                value = "notes",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
               // colors = textFieldColors
            )
        }
    }
}


@Preview
@Composable
fun AddEditExpPreview() {
    AddTranScreen(
       onAmtChange = { }
    )
}