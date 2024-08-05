package com.piyushprajpti.pyclock.presentation.main_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushprajpti.pyclock.ui.theme.Typography

@Composable
fun TopBar(
    title: String,
    onSettingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, top = 15.dp, end = 20.dp, bottom = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = Typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(
            onClick = { onSettingClick() },
            modifier = Modifier
                .background(Color.Transparent, CircleShape)
                .clip(CircleShape)
                .shadow(
                    elevation = 600.dp,
                    spotColor = Color(0x40000000),
                )
                .size(50.dp)

        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "settings",
                tint = Color.Black,
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .size(46.dp)
                    .padding(6.dp)
            )
        }
    }
}