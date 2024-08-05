package com.piyushprajpti.pyclock.presentation.clock_screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockCanvas(
    second: Int,
    minute: Int,
    hour: Int,
    outerColor: Color,
    innerColor: Color,
    primaryColor: Color,
    modifier: Modifier
) {

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = minOf(canvasWidth, canvasHeight) / 2

        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        // Draw outer and inner circles
        drawCircle(color = outerColor, radius = radius, center = Offset(centerX, centerY))
        drawCircle(color = innerColor, radius = radius * 0.86f, center = Offset(centerX, centerY))
        drawCircle(color = outerColor, radius = radius * 0.45f, center = Offset(centerX, centerY))

        // Draw tick marks
        val tickAngles = listOf(0, 90, 180, 270)
        for (angle in tickAngles) {
            val radian = Math.toRadians(angle.toDouble())
            val startX = (centerX + radius * 0.83 * cos(radian)).toFloat()
            val startY = (centerY + radius * 0.83 * sin(radian)).toFloat()
            val endX = (centerX + radius * 0.73 * cos(radian)).toFloat()
            val endY = (centerY + radius * 0.73 * sin(radian)).toFloat()

            drawLine(
                color = primaryColor,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 6f
            )
        }

        // Calculate hand angles
        val secondAngle = Math.toRadians((second * 6 - 90).toDouble())
        val minuteAngle = Math.toRadians((minute * 6 - 90).toDouble())
        val hourAngle = Math.toRadians((hour % 12 * 30 + minute * 0.5 - 90))

        val secondHandLength = radius * 0.83f
        val minuteHandLength = radius * 0.70f
        val hourHandLength = radius * 0.45f

        // Draw second hand
        drawLine(
            color = Color.Red,
            start = Offset(centerX, centerY),
            end = Offset(
                (centerX + secondHandLength * cos(secondAngle)).toFloat(),
                (centerY + secondHandLength * sin(secondAngle)).toFloat()
            ),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )

        // Draw minute hand
        drawLine(
            color = primaryColor,
            start = Offset(centerX, centerY),
            end = Offset(
                (centerX + minuteHandLength * cos(minuteAngle)).toFloat(),
                (centerY + minuteHandLength * sin(minuteAngle)).toFloat()
            ),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Draw hour hand
        drawLine(
            color = primaryColor,
            start = Offset(centerX, centerY),
            end = Offset(
                (centerX + hourHandLength * cos(hourAngle)).toFloat(),
                (centerY + hourHandLength * sin(hourAngle)).toFloat()
            ),
            strokeWidth = 11f, // Thicker for better visibility
            cap = StrokeCap.Round
        )

        drawCircle(color = primaryColor, radius = radius * 0.03f, center = Offset(centerX, centerY))

    }
}