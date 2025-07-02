package com.example.infiltrados.ui.main.components
import android.media.MediaPlayer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onBackground,
    playSound: Boolean = false
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val soundPlayer = remember {
        MediaPlayer.create(context, R.raw.sonido_atras) // colocá tu sonido en res/raw/ui_click.mp3
    }

    IconButton(
        onClick = {
            if (playSound) soundPlayer.start()
            onClick()
        },
        modifier = modifier
            .size(48.dp)
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.rules_back),
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
    }
}
