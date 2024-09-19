package com.piyushprajpti.pyclock.presentation.setting_screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.util.DialogBox

@Composable
fun ThemePopup(
    currentTheme: Int,
    onOkClick: (selectedTheme: Int) -> Unit,
    onCancelClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val selectedTheme = remember {
        mutableIntStateOf(currentTheme)
    }

    DialogBox(
        title = "Select App Theme",
        content = {
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

            Spacer(modifier = Modifier.height(20.dp))
        },
        onCancelClick = {
            onCancelClick()
        },
        onOkClick = {
            onOkClick(selectedTheme.intValue)
        },
        boxWidth = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.4f else 0.8f
    )
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