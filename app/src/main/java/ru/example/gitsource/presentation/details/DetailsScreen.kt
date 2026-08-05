@file:OptIn(ExperimentalMaterial3Api::class)

package ru.example.gitsource.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.domain.details.DetailsEntity
import ru.example.gitsource.presentation.ImageRenderer
import ru.example.gitsource.presentation.ui.GraphicElementsDefaults.dividerThickness
import ru.example.gitsource.presentation.ui.PaddingDefaults.largePadding
import ru.example.gitsource.presentation.ui.PaddingDefaults.mediumPadding
import ru.example.gitsource.presentation.ui.PictureDefaults.mediumImageSize
import ru.example.gitsource.presentation.ui.PictureDefaults.smallImageSize
import ru.example.gitsource.presentation.ui.SpaceDefaults.mediumSpaceSize
import ru.example.gitsource.presentation.ui.SpaceDefaults.spaceSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.mediumFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.mediumStepFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallFontSize
import ru.example.gitsource.theme.GitSourceTheme

@Composable
internal fun DetailsScreen(
    state: DetailsUiState,
    modifier: Modifier = Modifier,
    onAction: (DetailsAction) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Row {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    title = {
                        BasicText(
                            maxLines = 1,
                            autoSize = TextAutoSize
                                .StepBased(
                                    minFontSize = smallFontSize,
                                    maxFontSize = mediumFontSize,
                                    stepSize = mediumStepFontSize
                                ),
                            text = state.details?.name ?: stringResource(R.string.loading),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { onAction(DetailsAction.NavigateBackClicked) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                                contentDescription = stringResource(R.string.back_arrow_description),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                thickness = dividerThickness,
                color = MaterialTheme.colorScheme.secondary,
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                state.details != null -> {
                    RepositoryDetailsContent(details = state.details)
                }
            }
        }
    }
}

@Composable
private fun RepositoryDetailsContent(
    modifier: Modifier = Modifier,
    details: DetailsEntity
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(mediumPadding)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spaceSize),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageRenderer(
                avatarUrl = details.avatarUrl,
                imageSize = mediumImageSize,
                fallbackResourceId = R.drawable.baseline_person_24,
                errorResourceId = R.drawable.outline_error_24
            )

            Spacer(modifier = Modifier.width(mediumSpaceSize))

            Text(
                text = stringResource(
                    id = R.string.owner_format_named,
                    formatArgs = arrayOf(details.ownerName)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_star_24),
                contentDescription = stringResource(R.string.stars_description),
                tint = Color.Yellow,
                modifier = Modifier.size(smallImageSize)
            )

            Spacer(modifier = Modifier.width(mediumSpaceSize))

            Text(
                text = stringResource(
                    id = R.string.amount_of_stars,
                    formatArgs = arrayOf(details.starsCount)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_code_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(smallImageSize)
            )

            Spacer(modifier = Modifier.width(mediumSpaceSize))

            Text(
                text = stringResource(
                    id = R.string.language,
                    formatArgs =
                        if (details.language != null) arrayOf(details.language)
                        else arrayOf(R.string.no_language_message)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }

        Column(modifier = modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.outline_description_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(smallImageSize)
                )

                Spacer(modifier = Modifier.width(mediumSpaceSize))

                Text(
                    text = stringResource(R.string.description),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!details.description.isNullOrBlank()) {
                Text(
                    text = details.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = largePadding)
                )
            } else {
                Text(
                    text = stringResource(R.string.no_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = largePadding)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_eye_tracking_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(smallImageSize)
            )

            Spacer(modifier = Modifier.width(mediumSpaceSize))

            Text(
                text = stringResource(
                    id = R.string.watchers_count,
                    formatArgs = arrayOf(details.watchersCount)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_circle_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(smallImageSize)
            )

            Spacer(modifier = Modifier.width(mediumSpaceSize))

            Text(
                text = stringResource(
                    id = R.string.issues_count,
                    formatArgs = arrayOf(details.openIssuesCount)
                ),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun PreviewDetailsScreen() {
    GitSourceTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DetailsScreen(
                state = DetailsUiState(
                    isLoading = false,
                    details = DetailsEntity(
                        id = 1,
                        name = "preview-repository",
                        starsCount = 123321,
                        language = "Kotlin",
                        ownerName = "developer444",
                        description = "Описание пустого репозитория preview-repository с неизвестным владельцем на несколько строк",
                        watchersCount = 99,
                        openIssuesCount = 10,
                        avatarUrl = "",
                    ),
                ),
                onAction = {}
            )
        }
    }
}