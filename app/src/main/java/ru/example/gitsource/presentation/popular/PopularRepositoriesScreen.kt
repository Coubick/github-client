@file:OptIn(ExperimentalMaterial3Api::class)

package ru.example.gitsource.presentation.popular

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
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
import ru.example.gitsource.domain.popular.PopularRepositoriesAction
import ru.example.gitsource.domain.popular.PopularRepositoriesUiState
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.defaults.CircularProgressIndicatorDefaults
import ru.example.gitsource.presentation.defaults.GraphicElementsDefaults.cardHeight
import ru.example.gitsource.presentation.defaults.GraphicElementsDefaults.dividerThickness
import ru.example.gitsource.presentation.defaults.GraphicElementsDefaults.mediumIconSize
import ru.example.gitsource.presentation.defaults.PictureDefaults.smallImageSize
import ru.example.gitsource.presentation.defaults.SpaceDefaults.mediumSpaceSize
import ru.example.gitsource.presentation.defaults.SpaceDefaults.smallSpaceSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.largeFontSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.minFontSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.smallestFontSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.smallFontSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.smallStepFontSize
import ru.example.gitsource.presentation.defaults.TextFieldDefaults.mediumStepFontSize
import ru.example.gitsource.theme.GitSourceTheme

@Composable
internal fun PopularRepositoriesScreen(
    modifier: Modifier = Modifier,
    onAction: (PopularRepositoriesAction) -> Unit,
    state: PopularRepositoriesUiState
) {
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.popular_repo_title),
                        )
                    }
                )
                HorizontalDivider(
                    thickness = dividerThickness,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },

        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = dividerThickness,
                    color = MaterialTheme.colorScheme.secondary
                )
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable(
                                    onClick = { onAction(PopularRepositoriesAction.SearchRepositoriesClicked) }
                                )
                                .size(mediumIconSize)
                                .weight(1f),
                            painter = painterResource(R.drawable.outline_search_24),
                            contentDescription = stringResource(R.string.search_description),

                            )

                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = smallSpaceSize),
                            thickness = dividerThickness,
                            color = MaterialTheme.colorScheme.secondary,
                        )

                        Icon(
                            modifier = Modifier
                                .clickable(
                                    onClick = { onAction(PopularRepositoriesAction.LogoutClicked) }
                                )
                                .size(mediumIconSize)
                                .weight(1f),
                            painter = painterResource(R.drawable.outline_logout_24),
                            contentDescription = stringResource(R.string.logout_descrpiption),
                        )

                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
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
                            PopularRepositoriesAction
                                .RepositoryCardClicked(repository.id)
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
                    minFontSize = minFontSize,
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
                        tint = Color.Yellow
                    )

                    BasicText(
                        text = starsCount.toString(),
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = smallestFontSize,
                            maxFontSize = smallFontSize,
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
                        maxFontSize = smallFontSize,
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
                    )
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