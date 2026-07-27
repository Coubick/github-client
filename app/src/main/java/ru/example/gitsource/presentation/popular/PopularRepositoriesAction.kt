package ru.example.gitsource.presentation.popular

import ru.example.gitsource.domain.popular.RepositoryCardEntity

internal sealed interface PopularRepositoriesAction {
    data class RepositoryCardClicked(
        val repository: RepositoryCardEntity
    ) : PopularRepositoriesAction
    data object LogoutClicked : PopularRepositoriesAction
    data object LogoutConfirmed : PopularRepositoriesAction
    data object LogoutDialogDismissed : PopularRepositoriesAction
    data object SearchRepositoriesClicked : PopularRepositoriesAction
}