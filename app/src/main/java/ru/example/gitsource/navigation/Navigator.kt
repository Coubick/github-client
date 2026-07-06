package ru.example.gitsource.navigation

import androidx.activity.OnBackPressedDispatcher
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class Navigator @Inject constructor() {
    private var navController: NavController? = null
    private lateinit var onBackPressedDispatcher: OnBackPressedDispatcher
    fun execute(command: Command) {
        when (command) {
            Command.BackCommand -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is Command.NavigateToCommand -> {
                val navigateTo = command.navigateTo
                navController?.navigate(navigateTo)
            }
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setonBackPressedDispatcher(onBackPressedDispatcher: OnBackPressedDispatcher) {
        this.onBackPressedDispatcher = onBackPressedDispatcher
    }
}