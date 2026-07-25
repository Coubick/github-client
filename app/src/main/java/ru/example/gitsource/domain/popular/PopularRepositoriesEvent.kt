package ru.example.gitsource.domain.popular


internal sealed interface PopularRepositoriesEvent {
    data object NavigateToRepositoriesSearch : PopularRepositoriesEvent
    data class NavigateToRepositoryCard(
        val repositoryName: String,
        val repositoryOwnerName: String
    ) : PopularRepositoriesEvent
    data class ShowError(val errorMessageResId: Int) : PopularRepositoriesEvent
    data object NavigateToAuth : PopularRepositoriesEvent
}