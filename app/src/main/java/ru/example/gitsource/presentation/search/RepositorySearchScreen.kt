@file:OptIn(ExperimentalMaterial3Api::class)

package ru.example.gitsource.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.example.gitsource.R
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.popular.RepositoryCard
import ru.example.gitsource.presentation.ui.CircularProgressIndicatorDefaults
import ru.example.gitsource.presentation.ui.PictureDefaults.mediumCornerShapeSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.lineHeight
import ru.example.gitsource.presentation.ui.TextFieldDefaults.mediumFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallFontSize
import ru.example.gitsource.presentation.ui.TextFieldDefaults.smallestFontSize
import ru.example.gitsource.theme.GitSourceTheme

@Composable
internal fun RepositorySearchScreen(
    modifier: Modifier = Modifier,
    onAction: (RepositorySearchAction) -> Unit,
    state: RepositorySearchState
) {
    var searchRequestText = state.searchRequestText
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { onAction(RepositorySearchAction.NavigateBackClicked) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                                contentDescription = stringResource(R.string.back_arrow_description),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    },
                    title = {
                        TextField(
                            value = searchRequestText,
                            onValueChange = { enteredText ->
                                searchRequestText = enteredText
                                onAction(RepositorySearchAction.RepoNameEntered(enteredText))
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = smallestFontSize,
                                lineHeight = lineHeight),
                            label = {
                                Text(
                                    text = stringResource(R.string.enter_request)
                                )
                            },
                            shape = RoundedCornerShape(mediumCornerShapeSize),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                            maxLines = 1,
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.searchRequestText.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(innerPadding),
                    text = stringResource(R.string.search_repo_text),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = mediumFontSize,
                    textAlign = TextAlign.Center
                )
            } else if (state.repositoriesList.isEmpty().not()) {
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
                                    RepositorySearchAction.RepositoryCardClicked(repository)
                                )
                            },
                        )
                    }
                }
            } else if (state.isLoading) {
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
            } else if (state.isFound.not() && state.searchRequestText.isEmpty().not()){
                Text(
                    modifier = Modifier
                        .padding(innerPadding),
                    text = stringResource(R.string.repositories_not_found),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = mediumFontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    GitSourceTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            RepositorySearchScreen(
                onAction = {},
                state = RepositorySearchState(
                    isLoading = false,
                    isFound = true,
                    repositoriesList = listOf(
                        RepositoryCardEntity(
                            id = 1,
                            name = "OneTwoThree",
                            starsCount = 222,
                            language = "Pascal",
                            authorName = "Enzo"
                        )
                    ),
                    searchRequestText = ""
                )
            )
        }
    }
}