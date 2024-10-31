package com.example.newnav.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBackIosNew
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newnav.R
import com.example.newnav.designsys.component.AylOutlinedTextField
import com.example.newnav.navigation.AylTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTranScreen(
    onAmtChange: (String) -> Unit,
    navController: NavHostController = rememberNavController(),
    viewModel: expTransViewModel = hiltViewModel()
)
{
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AylTopBar(
                titleRes = "Transaction",
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconContentDescription = "Navigation icon",
                onNavigationClick = {navController.popBackStack()},
                actionIcon = Icons.Default.MoreVert,
                actionIconContentDescription = "Action icon",
            )
        },
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
    ){ modifier ->
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
                label = { Text(text = "Amount") },
                value = if (state.amount == 0.0) "" else state.amount.toString(),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onAmtChange(it) },
                maxLines = 1,
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            AylOutlinedTextField(
                label = "Category",
                value = "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
            )
            AylOutlinedTextField(
                label = "Account",
                value = "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 1,
            )
            DateofTransaction(
                textFieldColors,
                viewModel
            )
            AylOutlinedTextField(
                label = "Notes",
                value = "",
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {  },
                maxLines = 3,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofTransaction(
    textFieldColors: TextFieldColors,
    viewModel: expTransViewModel = viewModel()
){

    val state by viewModel.state.collectAsState()

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: state.dateTrans

    OutlinedTextField(
        label = { Text(text = "Date") },
        value = selectedDate,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {
            viewModel.onDateUpdate(selectedDate)
        },
        maxLines = 1,
        //colors = textFieldColors,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        colors = textFieldColors
        )

    if (showDatePicker) {
        Popup(
            onDismissRequest = {
                showDatePicker = false
                viewModel.onDateUpdate(datePickerState.selectedDateMillis?.let {
                    convertMillisToDate(it)
                    }?: state.dateTrans )
            },
            alignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 64.dp)
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.outline)
                    .padding(16.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
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