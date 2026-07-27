package ru.example.gitsource.presentation.navigation

import ru.example.gitsource.presentation.Screen

internal sealed interface Command {
    data object BackCommand : Command
    data class NavigateToCommand(val screen: Screen) : Command

    data class NavigateToAndClearCommand(
        val screen: Screen,
        val clearToScreen: Screen
    ) : Command
}