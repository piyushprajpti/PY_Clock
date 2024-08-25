package com.piyushprajpti.pyclock.presentation.timer_screen

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun NumberInputField(
    label: String,
    value: String,
    onValueChange: (it: String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            val trimmedValue = if (it.length > 2) it.takeLast(2) else it
                onValueChange(trimmedValue)
        },
        label = { Text(text = label, fontSize = 18.sp) },
        colors = TextFieldDefaults.colors(
            cursorColor = VioletBlue,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            focusedIndicatorColor = VioletBlue,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = VioletBlue,
            unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            focusedTextColor = MaterialTheme.colorScheme.primary,
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
            fontSize = 26.sp
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier
            .widthIn(min = 50.dp, max = 80.dp)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
    )
}