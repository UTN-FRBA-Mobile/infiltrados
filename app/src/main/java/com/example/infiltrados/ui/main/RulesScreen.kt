package com.example.infiltrados.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.rules_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.rules_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RuleBlock(R.string.rules_objective_title, R.string.rules_objective_text)
            RuleBlock(R.string.rules_step1_title, R.string.rules_step1_text)
            RuleBlock(R.string.rules_step2_title, R.string.rules_step2_text)
            RuleBlock(R.string.rules_step3_title, R.string.rules_step3_text)
            RuleBlock(R.string.rules_step4_title, R.string.rules_step4_text)
            RuleBlock(R.string.rules_tips_title, R.string.rules_tips_text)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.rules_back))
            }
        }
    }
}

@Composable
private fun RuleBlock(titleRes: Int, bodyRes: Int) {
    Column {
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(bodyRes),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
