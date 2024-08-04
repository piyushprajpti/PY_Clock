package com.piyushprajpti.pyclock.presentation.setting_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyushprajpti.pyclock.presentation.CommonViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    selectedTheme: Int,
    onBackClick: () -> Unit,
    commonViewModel: CommonViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val linkTreeIntent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://linktr.ee/piyushprajpti")
        )
    }

    val showThemePopup = remember {
        mutableStateOf(false)
    }

    val themeName = when (selectedTheme) {
        2 -> "Light Theme"
        3 -> "Dark Theme"
        else -> "System Default"
    }

    fun onOkClick(i: Int) {
        showThemePopup.value = false

        coroutineScope.launch {
            commonViewModel.setTheme("selected_theme", i)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {

            // top bar
            SettingsTopBar(onBackClick = { onBackClick() })

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                title = "Current Time Zone",
                description = "GMT +5:30, Kolkata",
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                title = "App Theme",
                description = themeName,
                icon = Icons.Outlined.ChevronRight,
                onClick = { showThemePopup.value = true }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                title = "Connect with Developer",
                description = "Piyush Prajapati",
                icon = Icons.Outlined.ChevronRight,
                onClick = { localContext.startActivity(linkTreeIntent) }
            )
        }

        if (showThemePopup.value) {
            ThemePopup(
                currentTheme = selectedTheme,
                onOkClick = { onOkClick(it) },
                onCancelClick = { showThemePopup.value = false }
            )
        }
    }
}