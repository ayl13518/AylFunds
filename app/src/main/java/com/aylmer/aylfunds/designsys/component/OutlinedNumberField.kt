package com.aylmer.aylfunds.designsys.component


import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.aylmer.aylfunds.ui.theme.NewNavTheme
import com.aylmer.aylfunds.utils.DecimalFormatter
import com.aylmer.aylfunds.utils.DecimalInputVisualTransformation


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
    var value2: Double = 0.0
    value.toDoubleOrNull()?.let {
        value2 = value.toDouble()
    }
        OutlinedTextField(
            label = { Text(text = label) },
            value = if (value2 == 0.0) "" else value,
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

@ThemePreviews
@Composable
fun PreviewAylOutlinedNumber() {
    NewNavTheme {
        AylOutlinedNumber(
            label = "Ayl",
            value = "1,000.0",
            onValueChange = {}
        )
    }
}


