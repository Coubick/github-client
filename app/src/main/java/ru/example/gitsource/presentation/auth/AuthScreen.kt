package ru.example.gitsource.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import ru.example.gitsource.presentation.default_sizes.PictureDefaults.borderWidthMedium
import ru.example.gitsource.presentation.default_sizes.PictureDefaults.cornerShapeMedium
import ru.example.gitsource.presentation.default_sizes.PictureDefaults.shadowElevationHuge
import ru.example.gitsource.presentation.default_sizes.PictureDefaults.imageSizeMedium
import ru.example.gitsource.presentation.default_sizes.SpaceDefaults

@Composable
fun AuthScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.width(IntrinsicSize.Max),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.gitlogo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = Modifier
                        .size(imageSizeMedium)
                        .clip(
                            shape = RoundedCornerShape(cornerShapeMedium)
                        )
                        .border(
                            width = borderWidthMedium,
                            color = Color.Gray,
                            shape = RoundedCornerShape(cornerShapeMedium)
                        )
                        .shadow(
                            elevation = shadowElevationHuge,
                            shape = CircleShape,
                            clip = false,
                            ambientColor = Color.Gray,
                            spotColor = Color.Gray
                        )
                )

                Spacer(modifier = Modifier.height(SpaceDefaults.SpaceSize))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.login))
                }
            }
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