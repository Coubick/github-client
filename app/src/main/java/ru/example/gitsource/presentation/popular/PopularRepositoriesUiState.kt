package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal data class PopularRepositoriesUiState (
    val items: List<RepositoryCardEntity>,
    val isLoading: Boolean,
    val error: Throwable?,
    val endOfPaginationReached: Boolean,
    val isLogoutDialogVisible: Boolean,
)