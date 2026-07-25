package ru.example.gitsource.domain.popular

internal sealed interface PopularRepositoriesAction {
    data class RepositoryCardClicked(
        val repositoryName: String,
        val repositoryOwnerName: String
    ) : PopularRepositoriesAction
    data object LogoutClicked : PopularRepositoriesAction
    data object LogoutConfirmed : PopularRepositoriesAction
    data object LogoutDialogDismissed : PopularRepositoriesAction
    data object SearchRepositoriesClicked : PopularRepositoriesAction
}