package com.example.newnav.designsys.component


import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.newnav.ui.theme.NewNavTheme
import com.example.newnav.utils.DecimalFormatter
import com.example.newnav.utils.DecimalInputVisualTransformation


@Composable
fun AylOutlinedNumber(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
    )
    val decimalFormatter = DecimalFormatter()

    value.toDoubleOrNull()?.let {
        OutlinedTextField(
            label = { Text(text = label) },
            //placeholder = { Text(text = label) },
            value = if(it <= 0.0) "" else value,
            modifier = modifier,
            onValueChange = { onValueChange( decimalFormatter.cleanup(it))},
            maxLines = 1,
            colors = textFieldColors,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal),
            visualTransformation = DecimalInputVisualTransformation(decimalFormatter)
        )
    }
}

@ThemePreviews
@Composable
fun PreviewAylOutlinedNumber() {
    NewNavTheme {
        AylOutlinedNumber(
            label = "Ayl",
            value = "100",
            onValueChange = {}
        )
    }
}


