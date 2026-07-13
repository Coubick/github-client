package ru.example.gitsource.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.presentation.default_sizes.SpaceDefaults
import ru.example.gitsource.presentation.default_sizes.TextFieldDefaults

@Composable
fun AuthScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.title),
            fontSize = TextFieldDefaults.LargeFontSize
        )

        Spacer(modifier = Modifier.Companion.height(SpaceDefaults.SpaceSize))

        Button(onClick = onLoginClick) {
            Text(stringResource(R.string.login))
        }
    }
}

@Composable
@Preview
fun PreviewAuthScreen(){
    AuthScreen(
        onLoginClick = {}
    )
}