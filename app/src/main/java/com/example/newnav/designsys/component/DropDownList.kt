package com.example.newnav.designsys.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.newnav.ui.theme.NewNavTheme


@Composable
fun DropdownList(
    modifier: Modifier = Modifier,
    onTypeChange: (String) -> Unit = {},
    itemList: List<String> = emptyList(),
    label: String = ""
    ) {

    //val state = viewModel.state.collectAsState()
    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    //val itemList = state.value.accTypeList
    var selectedItem by rememberSaveable { mutableStateOf("") }

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

    // button
    OutlinedTextField(
        label = { Text(text = label) },
        readOnly = true,
        enabled = false,
        value = selectedItem,
        onValueChange = { onTypeChange(it) },
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = { showDropdown= !showDropdown }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDownCircle,
                    contentDescription = "Account Types"
                )
            }
        },
        modifier = modifier
            .clickable(onClick = { showDropdown= !showDropdown }),
        colors = textFieldColors,
    )


    // dropdown list
    Box(modifier = modifier) {
        if (showDropdown) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                // to dismiss on click outside
                onDismissRequest = {
                    showDropdown = false }
            ) {

                Column(
                    modifier = modifier
                        //.heightIn(max = 90.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .verticalScroll(state = scrollState)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    itemList.onEachIndexed { index, item ->
                        if (index != 0) {
                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Box(
                            modifier = modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    selectedItem = item
                                    onTypeChange(item)
                                    showDropdown = false
                                },
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(text = item,
                                modifier = Modifier.padding(start = 50.dp),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun DropdownListPreview()
{
    var itemList: List<String> = listOf("item1", "item2item", "item3")
    var modifier = Modifier.fillMaxWidth()

    NewNavTheme {
        Box(modifier = modifier

        ) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                // to dismiss on click outside
                onDismissRequest = { }
            ) {

                Column(
                    modifier = modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    itemList.onEachIndexed { index, item ->
                        if (index != 0) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Box(
                            modifier = modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable { },
                            contentAlignment = Alignment.CenterStart,

                        ) {
                            Text(text = item,
                                modifier = Modifier.padding(start = 50.dp),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                }
            }
        }
        }
    }


