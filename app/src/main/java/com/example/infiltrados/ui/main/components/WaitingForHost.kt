package com.example.infiltrados.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infiltrados.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun WaitingForHost(){
    Column{
        Text(
            text = stringResource(R.string.waiting_for_host),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
    }

}

@Preview
@Composable
fun WaitingForHostPreview() {
    WaitingForHost()
}