package com.piyushprajpti.pyclock.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun ActionButton(
    title: String,
    titleColor: Color,
    backColor: Color,
    onClick: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = 18.sp,
        color = titleColor,
        modifier = Modifier
            .background(backColor, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 30.dp, vertical = 12.dp)
    )
}

@Composable
fun PlayButton(
    onClick: () -> Unit
) {
    Icon(
        imageVector = Icons.Outlined.PlayArrow,
        contentDescription = "play",
        tint = Color.White,
        modifier = Modifier
            .background(VioletBlue, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .size(60.dp)
            .padding(8.dp)
    )
}

@Composable
fun DialogActionButton(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}