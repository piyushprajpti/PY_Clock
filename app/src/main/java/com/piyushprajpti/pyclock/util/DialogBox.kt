package com.piyushprajpti.pyclock.util

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogBox(
    title: String? = null,
    content: @Composable (() -> Unit),
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    usePlatformDefaultWidth: Boolean = true
) {
    Dialog(
        onDismissRequest = { onCancelClick() },
        properties = DialogProperties(usePlatformDefaultWidth = usePlatformDefaultWidth)
        ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                .padding(horizontal = if (!usePlatformDefaultWidth) 10.dp else 20.dp, vertical = 20.dp)
        ) {
            if (title != null) {
                Text(
                    text = title.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            content()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                DialogActionButton(title = "CANCEL", onClick = { onCancelClick() })

                Spacer(modifier = Modifier.width(20.dp))

                DialogActionButton(title = "OK", onClick = { onOkClick() })

            }
        }
    }
}