package ru.example.gitsource.navigation

import ru.example.gitsource.presentation.Screen

internal sealed interface Command {
    data object BackCommand : Command
    data class NavigateToCommand(val screen: Screen) : Command

    data class NavigateToAndPopUpTo(
        val screen: Screen,
        val clearUpTo: Screen
    ) : Command
}