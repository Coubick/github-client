package ru.example.gitsource.domain.popular

internal data class PopularRepositoriesUiState (
    val isLoading: Boolean = false,
    val repositoriesList: List<RepositoryCardEntity> = emptyList(),
    val isLogoutDialogVisible: Boolean = false
)