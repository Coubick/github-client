package ru.example.gitsource.navigation

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class Navigator @Inject constructor() {
    fun executeCommand(command: Command) {
        command.execute()
    }
}