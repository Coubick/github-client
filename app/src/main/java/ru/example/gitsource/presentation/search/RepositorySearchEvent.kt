package ru.example.gitsource.presentation.search

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal sealed interface RepositorySearchEvent {
    data class NavigateToRepositoryCard(val repository: RepositoryCardEntity) : RepositorySearchEvent
    data object NavigateBack : RepositorySearchEvent
    data class ShowError(val error: Int) : RepositorySearchEvent
}