package ru.example.gitsource.presentation.search

import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.UiText

internal sealed interface RepositorySearchEvent {
    data class NavigateToRepositoryCard(val repository: RepositoryCardEntity) : RepositorySearchEvent
    data object NavigateBack : RepositorySearchEvent
    data class ShowError(val message: UiText) : RepositorySearchEvent
}