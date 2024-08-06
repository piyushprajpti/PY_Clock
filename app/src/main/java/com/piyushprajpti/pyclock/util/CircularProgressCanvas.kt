package com.piyushprajpti.pyclock.util

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.piyushprajpti.pyclock.ui.theme.VioletBlue

@Composable
fun CircularProgressCanvas(
    modifier: Modifier,
    circumferenceColor: Color,
    centerColor: Color,
    progress: Float
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        val outerRadius = minOf(canvasWidth, canvasHeight) / 2
        val innerRadius = outerRadius * 0.86f

        drawCircle(color = circumferenceColor, radius = outerRadius, center = Offset(centerX, centerY))
        drawCircle(color = centerColor, radius = innerRadius, center = Offset(centerX, centerY))

        val arcThickness = (outerRadius - innerRadius)
        val arcRadius = outerRadius - arcThickness /2

        val arcRect = Rect(
            Offset(centerX - arcRadius, centerY - arcRadius),
            Size(arcRadius * 2, arcRadius * 2)
        )

        drawArc(
            color = VioletBlue,
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = arcThickness, cap = StrokeCap.Round)
        )
    }
}