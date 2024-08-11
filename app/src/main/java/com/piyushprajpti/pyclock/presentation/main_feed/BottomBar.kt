package com.piyushprajpti.pyclock.presentation.main_feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    iconsList: List<IconData>,
    activeButton: Int,
    onClick: (index: Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconsList.forEachIndexed { index, iconData ->

            val backColor = if (activeButton == index) MaterialTheme.colorScheme.primary
            else Color.Transparent

                Icon(
                    imageVector = iconData.icon,
                    contentDescription = iconData.iconName,
                    tint = if (activeButton == index) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(backColor, CircleShape)
                        .size(55.dp)
                        .clip(CircleShape)
                        .clickable { onClick(index) }
                        .padding(12.dp)
                )
            }
        }
    }

data class IconData(
    val icon: ImageVector,
    val iconName: String
)