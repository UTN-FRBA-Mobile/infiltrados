package com.example.infiltrados.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import com.example.infiltrados.R

@Composable
fun LobbyScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.headlineMedium
        )
        Button(
            onClick = {
                navController.navigate("input")
            }
        ) {
            Text(stringResource(R.string.begin))
        }
        Button(
            onClick = {
                navController.navigate("rules")
            }
        ) {
            Text(stringResource(R.string.view_rules))
        }

    }

}