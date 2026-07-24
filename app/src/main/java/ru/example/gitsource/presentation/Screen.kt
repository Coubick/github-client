package ru.example.gitsource.presentation

import ru.example.gitsource.R

internal sealed interface Screen {
    data object AuthScreen : Screen
    data object PopularRepositoriesScreen : Screen
    data object StartScreen : Screen
    data class RepositoryCardScreen(val repositoryCardId: Int) : Screen
    data object SearchRepositoryScreen : Screen

}

internal fun Screen.toDestinationId(): Int {
    return when (this) {
        is Screen.AuthScreen -> R.id.authFragment
        is Screen.PopularRepositoriesScreen -> R.id.popularRepositoriesFragment
        is Screen.StartScreen -> R.id.startFragment
        is Screen.RepositoryCardScreen -> R.id.repositoryDetailsFragment
        is Screen.SearchRepositoryScreen -> R.id.searchRepositoryFragment
    }
}