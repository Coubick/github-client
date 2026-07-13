package ru.example.gitsource.presentation

import ru.example.gitsource.R

internal sealed interface Screen {
    data object AuthScreen : Screen
    data object PopularRepositoriesScreen : Screen

}

internal fun Screen.toDestinationId(): Int {
    return when (this) {
        is Screen.AuthScreen -> R.id.authFragment
        is Screen.PopularRepositoriesScreen -> R.id.popularRepositoriesFragment
    }
}