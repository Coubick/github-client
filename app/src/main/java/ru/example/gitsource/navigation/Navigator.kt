package ru.example.gitsource.navigation

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.R
import ru.example.gitsource.presentation.screens.toDestinationId
import javax.inject.Inject

@ActivityScoped
internal class Navigator @Inject constructor() {
    private var navController: NavController? = null
    private lateinit var onBackPressedDispatcher: OnBackPressedDispatcher

    private var activity: AppCompatActivity? = null

    fun execute(command: Command) {
        when (command) {
            Command.BackCommand -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is Command.NavigateToCommand -> {
                val navigateTo = command.screen.toDestinationId()
                navController?.navigate(navigateTo)
            }
        }
    }

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setOnBackPressedDispatcher(onBackPressedDispatcher: OnBackPressedDispatcher) {
        this.onBackPressedDispatcher = onBackPressedDispatcher
    }

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    fun setupBackPressedHandler() {
        onBackPressedDispatcher.addCallback(activity) {
            val currentDestination = navController?.currentDestination?.id
            if (currentDestination == R.id.authFragment) {
                activity?.finish()
            } else {
                navController?.popBackStack()
            }
        }
    }
}