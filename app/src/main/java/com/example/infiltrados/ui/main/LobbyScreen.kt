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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.ServerTestPanel

@Composable
fun LobbyScreen(navController: NavController) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFBA68C8),
            Color(0xFF5a1366)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {

        LanguageSelector { selectedLanguage ->
            Log.d("Idioma", "Elegiste: $selectedLanguage")
        }

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

            LobbyButton(
                text = stringResource(R.string.begin),
                navController = navController,
                destination = "input"
            )

            LobbyButton(
                text = stringResource(R.string.join_game),
                navController = navController,
                destination = "lobby"
            )

            LobbyButton(
                text = stringResource(R.string.view_rules),
                navController = navController,
                destination = "rules"
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

@Composable
fun LanguageSelector(
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf("es") }

    val currentFlag = when (currentLanguage) {
        "en" -> R.drawable.ic_us
        "es" -> R.drawable.ic_spain
        else -> R.drawable.ic_spain
    }

    Box(
        modifier = Modifier
            .padding(12.dp, top = 48.dp)
            .wrapContentSize(Alignment.TopStart)
    ) {
        Button(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Transparent
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(id = currentFlag),
                contentDescription = "Idioma actual",
                modifier = Modifier.fillMaxSize()
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    currentLanguage = "es"
                    onLanguageSelected("es")
                    expanded = false
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_spain),
                            contentDescription = "Español",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Español")
                    }
                }
            )
            DropdownMenuItem(
                onClick = {
                    currentLanguage = "en"
                    onLanguageSelected("en")
                    expanded = false
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_us),
                            contentDescription = "English",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("English")
                    }
                }
            )
        }
    }
}
