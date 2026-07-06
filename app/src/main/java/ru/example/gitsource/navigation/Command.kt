package ru.example.gitsource.navigation

internal sealed interface Command {
    data object BackCommand : Command
    data class NavigateToCommand(val navigateTo: Int) : Command
}