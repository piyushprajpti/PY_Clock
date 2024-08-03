package com.piyushprajpti.pyclock.presentation.main_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainFeed(
    onSettingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(title = "Clock", onSettingClick = { onSettingClick() })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {

        }
    }
}