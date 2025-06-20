package com.example.infiltrados.ui.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.components.GameButton
import com.example.infiltrados.ui.components.ServerTestPanel

@Composable
fun LobbyScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFFAF5FB))
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_app_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(), // 👈 solo para testeo
            alpha = 1f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_infiltrados),
                contentDescription = stringResource(R.string.greeting),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 20.dp)
            )

             GameButton(
                text = stringResource(R.string.begin),
                 onClick = { navController.navigate("input") }
            )

            GameButton(
                text = stringResource(R.string.join_game),
                onClick = { navController.navigate("lobby") }
            )

            GameButton(
                text = stringResource(R.string.view_rules),
                onClick = { navController.navigate("rules") }
            )

            ServerTestPanel()
        }
    }

}

@Composable
fun LobbyButton(
    text: String,
    navController: NavController,
    destination: String
) {
    Button(
        onClick = { navController.navigate(destination) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF8c68b8),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(80.dp)
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
