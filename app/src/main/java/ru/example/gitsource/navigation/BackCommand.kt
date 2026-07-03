package ru.example.gitsource.navigation

import androidx.activity.OnBackPressedDispatcher

internal class BackCommand(
    private val onBackPressedDispatcher: OnBackPressedDispatcher
) : Command {
    override fun execute() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()){
            onBackPressedDispatcher.onBackPressed()
        }
    }
}