package ru.example.gitsource.domain.popular

internal sealed interface PopularRepositoriesAction {
    data object RepositoryCardClicked
    data object LogoutClicked
    data object SearchRepositoriesClicked
}