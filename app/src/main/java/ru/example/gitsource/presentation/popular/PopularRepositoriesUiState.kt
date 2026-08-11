package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.paging.Paginator

internal data class PopularRepositoriesUiState (
    val paginator: Paginator<RepositoryCardEntity>,
    val isLogoutDialogVisible: Boolean,
)