package com.example.infiltrados.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.background


@Composable
fun SplashScreen(navController: NavController) {
    val isDark = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("lobby") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val logoRes = if (isDark) R.drawable.logo_dark else R.drawable.logo_light
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Logo Infiltrados",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            DotsLoadingAnimation()
        }
    }
}

@Composable
fun DotsLoadingAnimation() {
    val dotCount = 3
    val infiniteTransition = rememberInfiniteTransition(label = "dotTransition")

    val animations = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing, delayMillis = index * 150),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot$index"
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        animations.forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .graphicsLayer { this.alpha = alpha.value }
                    .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
            )
        }
    }
}
