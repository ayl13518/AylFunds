package com.example.newnav.designsys.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newnav.viewmodels.AccountViewModel


@Composable
fun DropdownList(
    viewModel: AccountViewModel= hiltViewModel(),
    ) {

    val state = viewModel.state.collectAsState()
    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val itemList = state.value.accTypeList

    // button
    OutlinedTextField(
        label = { Text(text = "Type") },
        value = state.value.accType,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { viewModel.onAccTypeChange(state.value.accType) },
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = { showDropdown= !showDropdown }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDownCircle,
                    contentDescription = "Account Types"
                )
            }
        },
        //colors = textFieldColors,
    )


    // dropdown list
    Box {
        if (showDropdown) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                // to dismiss on click outside
                onDismissRequest = { showDropdown = false }
            ) {

                Column(
                    modifier = Modifier
                        //.heightIn(max = 90.dp)
                        .verticalScroll(state = scrollState)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    itemList.onEachIndexed { index, item ->
                        if (index != 0) {
                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onAccTypeChange(item)
                                    showDropdown = false
                                },
                            contentAlignment = Alignment.Center,

                        ) {
                            Text(text = item)
                        }
                    }

                }
            }
        }
    }
}

