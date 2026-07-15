package ru.example.gitsource.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.presentation.defaultSizes.PictureDefaults.borderWidthMedium
import ru.example.gitsource.presentation.defaultSizes.PictureDefaults.cornerShapeMedium
import ru.example.gitsource.presentation.defaultSizes.PictureDefaults.imageSizeMedium
import ru.example.gitsource.presentation.defaultSizes.PictureDefaults.shadowElevationHuge
import ru.example.gitsource.presentation.defaultSizes.SpaceDefaults

@Composable
fun AuthScreen(modifier: Modifier = Modifier, onLoginClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
                Image(
                    painter = painterResource(R.drawable.gitlogo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = modifier
                        .size(imageSizeMedium)
                        .clip(
                            shape = RoundedCornerShape(cornerShapeMedium),
                        )
                        .border(
                            width = borderWidthMedium,
                            color = Color.Gray,
                            shape = RoundedCornerShape(cornerShapeMedium),
                        )
                        .shadow(
                            elevation = shadowElevationHuge,
                            shape = CircleShape,
                            clip = false,
                            ambientColor = Color.LightGray,
                            spotColor = Color.LightGray,
                        )
                )

                Spacer(modifier = modifier.height(SpaceDefaults.SpaceSize))

                Button(
                    onClick = onLoginClick,
                    modifier = modifier.width(imageSizeMedium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.login),
                    )
                }
            }

}

@Composable
@Preview
fun PreviewAuthScreen() {
    AuthScreen(
        onLoginClick = {}
    )
}