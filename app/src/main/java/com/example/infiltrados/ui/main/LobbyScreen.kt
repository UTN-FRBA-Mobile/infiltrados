package com.example.infiltrados.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.components.InfiltradosBackground
import com.example.infiltrados.ui.components.OptionCard

@Composable
fun LobbyScreen(navController: NavController) {
    InfiltradosBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(R.drawable.logo_infiltrados),
                contentDescription = stringResource(R.string.greeting),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 32.dp)
                ) {
                    OptionCard(
                        icon = Icons.Default.PlayArrow,
                        title = stringResource(R.string.begin),
                        description = stringResource(R.string.begin_description)
                    ) {
                        navController.navigate("game_selection")
                    }

                    OptionCard(
                        icon = Icons.Default.Group,
                        title = stringResource(R.string.join_game),
                        description = stringResource(R.string.join_game_description)
                    ) {
                        navController.navigate("joinGame")
                    }

                    OptionCard(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.view_rules),
                        description = stringResource(R.string.view_rules_description)
                    ) {
                        navController.navigate("rules")
                    }
                }

                //GameButton(text = stringResource(R.string.begin), onClick = { navController.navigate("game_selection") })
                //GameButton(text = stringResource(R.string.join_game), onClick = { navController.navigate("join_game") })
                //GameButton(text = stringResource(R.string.view_rules), onClick = { navController.navigate("rules") })

                Spacer(modifier = Modifier.height(16.dp))

                // ServerTestPanel()
            }
        }
    }
}