package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity


internal sealed interface PopularRepositoriesEvent {
    data object NavigateToRepositoriesSearch : PopularRepositoriesEvent
    data class NavigateToRepositoryCard(val repository: RepositoryCardEntity) : PopularRepositoriesEvent
    data class ShowError(val errorMessageResId: Int) : PopularRepositoriesEvent
    data object NavigateToAuth : PopularRepositoriesEvent
}