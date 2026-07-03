package ru.example.gitsource.navigation

import androidx.navigation.NavController

internal class NavigateToCommand(
    private val destinationId: Int,
    private val navController: NavController
) : Command {
    override fun execute() {
        navController.navigate(destinationId)
    }
}