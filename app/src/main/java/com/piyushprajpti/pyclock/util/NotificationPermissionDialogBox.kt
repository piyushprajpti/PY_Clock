package com.piyushprajpti.pyclock.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.piyushprajpti.pyclock.ui.theme.ErrorRed

@Composable
fun NotificationPermissionDialogBox(onDismissRequest: () -> Unit) {

    val context = LocalContext.current

    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Attention User!!!",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textDecoration = TextDecoration.Underline,
                color = ErrorRed
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please grant notification permission for the app to function properly",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Go To Settings",
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .clickable {
                        context.startActivity(settingsIntent)
                    }
                    .padding(10.dp)
            )
        }
    }
}