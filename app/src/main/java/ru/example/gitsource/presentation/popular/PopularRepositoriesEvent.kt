package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.UiText


internal sealed interface PopularRepositoriesEvent {
    data object NavigateToRepositoriesSearch : PopularRepositoriesEvent
    data class NavigateToRepositoryCard(val repository: RepositoryCardEntity) : PopularRepositoriesEvent
    data class ShowError(val message: UiText) : PopularRepositoriesEvent
    data object NavigateToAuth : PopularRepositoriesEvent
}