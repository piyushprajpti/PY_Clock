package com.piyushprajpti.pyclock.presentation.setting_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.piyushprajpti.pyclock.util.DialogActionButton

@Composable
fun ThemePopup(
    currentTheme: Int,
    onOkClick: (selectedTheme: Int) -> Unit,
    onCancelClick: () -> Unit
) {

    val selectedTheme = remember {
        mutableIntStateOf(currentTheme)
    }

    Dialog(
        onDismissRequest = { onCancelClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Select App Theme",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Options(
                title = "System Default",
                isSelected = selectedTheme.intValue == 1,
                onClick = { selectedTheme.intValue = 1 }
            )
            Options(
                title = "Light Theme",
                isSelected = selectedTheme.intValue == 2,
                onClick = { selectedTheme.intValue = 2 }
            )
            Options(
                title = "Dark Theme",
                isSelected = selectedTheme.intValue == 3,
                onClick = { selectedTheme.intValue = 3 }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                DialogActionButton(title = "CANCEL", onClick = { onCancelClick() })

                Spacer(modifier = Modifier.width(25.dp))

                DialogActionButton(title = "OK", onClick = { onOkClick(selectedTheme.intValue) })

            }
        }
    }
}

@Composable
fun Options(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = { onClick() })
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}