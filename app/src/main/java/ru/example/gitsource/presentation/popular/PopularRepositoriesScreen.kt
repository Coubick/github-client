@file:OptIn(ExperimentalMaterial3Api::class)

package ru.example.gitsource.presentation.popular

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.ui.CircularProgressIndicatorDefaults
import ru.example.gitsource.presentation.ui.GraphicElementsDefaults.cardHeight
import ru.example.gitsource.presentation.ui.GraphicElementsDefaults.dividerThickness
import ru.example.gitsource.presentation.ui.PictureDefaults.smallImageSize
import ru.example.gitsource.presentation.ui.SpaceDefaults.mediumSpaceSize
import ru.example.gitsource.presentation.ui.SpaceDefaults.smallSpaceSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.largeFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.mediumStepFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.mediumFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallStepFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallestFontSize
import ru.example.gitsource.theme.GitSourceTheme

@Composable
internal fun PopularRepositoriesScreen(
    modifier: Modifier = Modifier,
    onAction: (PopularRepositoriesAction) -> Unit,
    state: PopularRepositoriesUiState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
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
                            text = stringResource(R.string.popular_repo_title),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { onAction(PopularRepositoriesAction.SearchRepositoriesClicked) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_search_24),
                                contentDescription = stringResource(R.string.search_description),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        IconButton(
                            onClick = { onAction(PopularRepositoriesAction.LogoutClicked) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_logout_24),
                                contentDescription = stringResource(R.string.logout_descrpiption),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                )
                HorizontalDivider(
                    thickness = dividerThickness,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            state = rememberLazyListState(),
        ) {
            items(
                items = state.repositoriesList,
                key = { it.id },
            ) { repository ->
                RepositoryCard(
                    name = repository.name,
                    starsCount = repository.starsCount,
                    language = repository.language,
                    onRepositoryCardClicked = {
                        onAction(
                            PopularRepositoriesAction.RepositoryCardClicked(repository)
                        )
                    },
                )
            }
        }

        if (state.isLogoutDialogVisible) {
            ConfirmExitDialog(
                onDismissRequest = { onAction(PopularRepositoriesAction.LogoutDialogDismissed) },
                onConfirmation = { onAction(PopularRepositoriesAction.LogoutConfirmed) },
            )
        }

        if (state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(CircularProgressIndicatorDefaults.smallSize)
                        .align(Alignment.Center),
                    strokeWidth = CircularProgressIndicatorDefaults.smallStroke
                )
            }
        }
    }

    Spacer(
        Modifier.size(mediumSpaceSize)
    )
}

@Composable
internal fun RepositoryCard(
    modifier: Modifier = Modifier,
    name: String,
    starsCount: Int,
    language: String?,
    onRepositoryCardClicked: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(smallSpaceSize)
            .clickable(
                onClick = onRepositoryCardClicked
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_person_24),
                modifier = Modifier
                    .size(smallImageSize)
                    .clip(CircleShape)
                    .weight(0.5f),
                contentDescription = stringResource(R.string.avatar),
                tint = MaterialTheme.colorScheme.tertiary
            )

            BasicText(
                modifier = Modifier
                    .weight(3f),
                text = name,
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = smallFontSize,
                    maxFontSize = largeFontSize,
                    stepSize = mediumStepFontSize,
                ),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                )
            )

            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(end = smallSpaceSize),
                horizontalAlignment = Alignment.End
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.baseline_star_24),
                        contentDescription = stringResource(R.string.stars_description),
                        tint = Color(0xFFFFD700)
                    )

                    BasicText(
                        text = starsCount.toString(),
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = smallestFontSize,
                            maxFontSize = mediumFontSize,
                            stepSize = smallStepFontSize
                        ),
                        maxLines = 1,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                    )
                }

                Spacer(
                    Modifier.size(smallSpaceSize)
                )

                BasicText(
                    text = language ?: stringResource(R.string.no_language_message),
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = smallestFontSize,
                        stepSize = smallStepFontSize,
                        maxFontSize = mediumFontSize,
                    ),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun ConfirmExitDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.confirm_exit),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = stringResource(R.string.dismiss),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPopularRepoScreen() {
    GitSourceTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            PopularRepositoriesScreen(
                onAction = {},
                state = PopularRepositoriesUiState(
                    repositoriesList = listOf(
                        RepositoryCardEntity(
                            id = 1,
                            name = "simple-project-repo",
                            starsCount = 345,
                            language = "assembly",
                            authorName = "tourist",
                        )
                    ),
                    isLoading = false,
                    isLogoutDialogVisible = false
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewRepositoryCard() {
    GitSourceTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            RepositoryCard(
                name = "TestRepo",
                starsCount = 68,
                language = "C++",
                onRepositoryCardClicked = {},
            )
        }
    }
}