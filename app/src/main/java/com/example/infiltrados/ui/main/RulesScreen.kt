package com.example.infiltrados.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.UndercoverButton

@Composable
fun RulesScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo animado
        AnimatedBackground()

        // Capa de fondo sólida para asegurar contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de volver
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.rules_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.rules_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            RuleBlock(R.string.rules_objective_title, R.string.rules_objective_text)
            RuleBlock(R.string.rules_step1_title, R.string.rules_step1_text)
            RuleBlock(R.string.rules_step2_title, R.string.rules_step2_text)
            RuleBlock(R.string.rules_step3_title, R.string.rules_step3_text)
            RuleBlock(R.string.rules_step4_title, R.string.rules_step4_text)
            RuleBlock(R.string.rules_tips_title, R.string.rules_tips_text)

            Spacer(modifier = Modifier.height(24.dp))

            UndercoverButton(
                text = stringResource(R.string.rules_back),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun RuleBlock(titleRes: Int, bodyRes: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(bodyRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
