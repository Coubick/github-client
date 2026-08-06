package ru.example.gitsource.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkConstants.MAX_RETRIES
import ru.example.gitsource.data.network.NetworkConstants.RETRY_DELAY
import ru.example.gitsource.data.network.NetworkUtils
import ru.example.gitsource.presentation.ui.CircularProgressIndicatorDefaults.smallIndicatorStroke

@Composable
fun ImageRenderer(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    imageSize: Dp,
    fallbackResourceId: Int,
    errorResourceId: Int,
) {
    var isError by remember { mutableStateOf(false) }
    var retryCount by remember { mutableIntStateOf(0) }

    val isConnected = NetworkUtils.rememberNetworkConnectivity()

    val imageKeyForReload = remember(avatarUrl, retryCount) {
        if (retryCount > 0) {
            "$avatarUrl?retry=$retryCount}"
        } else {
            avatarUrl
        }
    }

    LaunchedEffect(isConnected, isError) {
        if (isConnected && isError && retryCount < MAX_RETRIES) {
            delay(RETRY_DELAY)
            retryCount++
            isError = false
        }
    }

    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = imageKeyForReload,
            fallback = painterResource(fallbackResourceId),
            error = painterResource(errorResourceId),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            contentDescription = stringResource(R.string.avatar),
            onLoading = {
                isLoading = true
                isError = false
            },
            onSuccess = {
                isLoading = false
                isError = false
            },
            onError = {
                isLoading = false
                isError = true
            },
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(imageSize)
                    .align(Alignment.Center),
                strokeWidth = smallIndicatorStroke,
            )
        }
    }
}

