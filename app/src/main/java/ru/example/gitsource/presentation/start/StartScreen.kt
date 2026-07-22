package ru.example.gitsource.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.presentation.defaults.CircularProgressIndicatorDefaults

@Composable
internal fun StartScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(CircularProgressIndicatorDefaults.smallSize),
            strokeWidth = CircularProgressIndicatorDefaults.smallStroke
        )
    }
}

@Composable
@Preview
fun PreviewStartScreen(){
    StartScreen()
}