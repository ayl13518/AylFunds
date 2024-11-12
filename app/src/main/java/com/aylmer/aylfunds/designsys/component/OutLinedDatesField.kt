package com.aylmer.aylfunds.designsys.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aylmer.aylfunds.utils.convertMillisToDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDatesField(
    defaultDate: String,
    onValueChange: (String) -> Unit={},
){
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: defaultDate

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    OutlinedTextField(
        label = { Text(text = "Date") },
        value = selectedDate,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {
            onValueChange(selectedDate)
        },
        maxLines = 1,
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
}