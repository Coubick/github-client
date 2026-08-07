@file:OptIn(ExperimentalMaterial3Api::class)

package ru.example.gitsource.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import ru.example.gitsource.theme.GitSourceTheme

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RepositorySearchScreen(
    onAction: (RepositorySearchAction) -> Unit,
    state: RepositorySearchState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible = WindowInsets.isImeVisible
    val focusRequester = remember { FocusRequester() }

    var isFirstLaunch by remember { mutableStateOf(true) }

    LaunchedEffect(
        key1 = isKeyboardVisible,
        key2 = state.searchRequestText
    ) {
        if (isFirstLaunch) {
            if (isKeyboardVisible) {
                isFirstLaunch = false
            }
            return@LaunchedEffect
        }

        if (isKeyboardVisible.not() && state.searchRequestText.isEmpty()) {
            onAction(RepositorySearchAction.NavigateBack)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .imePadding(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { onAction(RepositorySearchAction.NavigateBack) }
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
                            value = state.searchRequestText,
                            onValueChange = { enteredText ->
                                onAction(RepositorySearchAction.RepoNameEntered(enteredText))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = smallFontSize,
                                lineHeight = lineHeight,
                            ),
                            label = {
                                Text(
                                    text = stringResource(R.string.enter_request),
                                )
                            },
                            shape = RoundedCornerShape(mediumCornerShapeSize),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.6f
                                ),
                            ),
                            singleLine = true,
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
            when {
                state.searchRequestText.isEmpty() -> Text(
                    modifier = Modifier
                        .padding(innerPadding),
                    text = stringResource(R.string.search_repo_text),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = mediumFontSize,
                    textAlign = TextAlign.Center,
                )

                state.searchRequestText.isEmpty() ->
                    Text(
                        modifier = Modifier
                            .padding(innerPadding),
                        text = stringResource(R.string.search_repo_text),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = mediumFontSize,
                        textAlign = TextAlign.Center,
                    )

                state.repositoriesList.isEmpty().not() ->
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
                                avatarUrl = repository.avatarUrl,
                            )
                        }
                    }

                state.isLoading ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(CircularProgressIndicatorDefaults.smallIndicatorSize)
                                .align(Alignment.Center),
                            strokeWidth = CircularProgressIndicatorDefaults.smallIndicatorStroke,
                        )
                    }

                state.isFound.not() ->
                    Text(
                        modifier = Modifier
                            .padding(innerPadding),
                        text = stringResource(R.string.repositories_not_found),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = mediumFontSize,
                        textAlign = TextAlign.Center,
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
                            authorName = "Enzo",
                            avatarUrl = "https://avatars.githubusercontent.com/u/144241203?v=4",
                        )
                    ),
                    searchRequestText = "",
                )
            )
        }
    }
}