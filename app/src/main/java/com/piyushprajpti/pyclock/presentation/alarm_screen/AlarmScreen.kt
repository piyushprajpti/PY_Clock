package com.piyushprajpti.pyclock.presentation.alarm_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun AlarmScreen(
    onAlarmCardClick: () -> Unit,
) {


    fun onAddAlarmClick() {
        onAlarmCardClick()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

        }

        // add alarm FAB
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add alarm",
            tint = Color.White,
            modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.BottomCenter)
                .background(VioletBlue, CircleShape)
                .clip(CircleShape)
                .size(60.dp)
                .clickable { onAddAlarmClick() }
                .padding(10.dp)
        )

    }
}