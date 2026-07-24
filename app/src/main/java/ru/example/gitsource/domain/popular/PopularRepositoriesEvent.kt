package ru.example.gitsource.domain.popular


internal sealed interface PopularRepositoriesEvent {
    data object NavigateToRepositoriesSearch : PopularRepositoriesEvent
    data class NavigateToRepositoryCard(val repositoryId: Int) : PopularRepositoriesEvent
    data class ShowError(val errorMessageResId: Int) : PopularRepositoriesEvent
    data object NavigateToAuth : PopularRepositoriesEvent
}