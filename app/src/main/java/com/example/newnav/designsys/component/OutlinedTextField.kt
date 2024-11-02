package com.example.newnav.designsys.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.newnav.ui.theme.NewNavTheme


@Composable
fun AylOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) {

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
    )

    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        modifier = modifier,
        onValueChange = { onValueChange(it) },
        maxLines = maxLines,
        colors = textFieldColors,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium
    )

}

@ThemePreviews
@Composable
fun AylOutlinedTextFieldPreview() {
    NewNavTheme {
        AylOutlinedTextField(
            label = "Ayl",
            value = "val",
            onValueChange = {}
        )
    }
}