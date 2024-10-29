package com.example.newnav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newnav.viewmodels.expTransViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState

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
            val textFieldColors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                //cursorColor = MaterialTheme.colorScheme.colors.secondary.copy(alpha = ContentAlpha.high)
            )

           OutlinedTextField(
                label = { Text(text = "Amount") },
                value = state.amount.toString(),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAmtChange(it) },
                maxLines = 1,
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = "category",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = "account",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
                colors = textFieldColors
            )
            DateofTransaction()
//            OutlinedTextField(
//                label = { Text(text = "Date") },
//                value = state.dateTrans,
//                modifier = Modifier.fillMaxWidth(),
//                onValueChange = { },
//                maxLines = 1,
//                colors = textFieldColors,
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = "Select date"
//                    )
//                },
//            )
            OutlinedTextField(
                value = "notes",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
                colors = textFieldColors
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofTransaction(){

    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    OutlinedTextField(
        label = { Text(text = "Date") },
        value = selectedDate,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { },
        maxLines = 1,
        //colors = textFieldColors,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date"
            )
        },

        )
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Preview
@Composable
fun AddEditExpPreview() {
    AddTranScreen(
       onAmtChange = { }
    )
}