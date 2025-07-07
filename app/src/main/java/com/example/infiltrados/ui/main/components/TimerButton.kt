package com.example.infiltrados.ui.main.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TimerButton(
    seconds: Int,
    secondsRemaining: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = secondsRemaining.toFloat() / seconds,
        animationSpec = tween(durationMillis = 500),
        label = "TimerProgress"
    )

    val progressColor = when {
        secondsRemaining > seconds * 0.5 -> Color(0xFF4CAF50)
        secondsRemaining > seconds * 0.25 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            // Fondo
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFFEEEEEE),
                    style = Stroke(width = 12f)
                )
            }

            // Progreso
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360 * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = 12f, cap = StrokeCap.Round)
                )
            }

            // Texto
            Text(
                text = "$secondsRemaining s",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun TimerButtonPreview() {
    TimerButton(
        seconds = 60,
        secondsRemaining = 30
    )
}