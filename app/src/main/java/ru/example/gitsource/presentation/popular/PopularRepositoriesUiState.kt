package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal data class PopularRepositoriesUiState (
    val isLoading: Boolean,
    val repositoriesList: List<RepositoryCardEntity>,
    val isLogoutDialogVisible: Boolean
)