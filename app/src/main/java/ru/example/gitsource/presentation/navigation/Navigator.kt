package ru.example.gitsource.presentation.navigation

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.navOptions
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.R
import ru.example.gitsource.domain.OAuthLauncher
import ru.example.gitsource.presentation.Screen
import ru.example.gitsource.presentation.toDestinationId
import javax.inject.Inject

@ActivityScoped
internal class Navigator @Inject constructor(
    private val oAuthLauncher: OAuthLauncher
) {
    private var navController: NavController? = null
    private lateinit var onBackPressedDispatcher: OnBackPressedDispatcher
    private var activity: AppCompatActivity? = null

    private companion object {
        const val REPOSITORY_NAME_KEY = "repositoryName"
        const val REPOSITORY_OWNER_NAME = "repositoryOwnerName"
    }

    private fun makeBundle(screen: Screen): Bundle? {
        val bundle = when (val screen = screen) {
            is Screen.RepositoryCardScreen -> {
                bundleOf(
                    REPOSITORY_NAME_KEY to screen.repositoryName,
                    REPOSITORY_OWNER_NAME to screen.repositoryOwnerName
                )
            }

            else -> {
                null
            }
        }

        return bundle
    }

    fun execute(command: Command) {
        when (command) {
            Command.BackCommand -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is Command.NavigateToCommand -> {
                val navigateTo = command.screen.toDestinationId()
                val bundle = makeBundle(command.screen)
                navController?.navigate(navigateTo, bundle)
            }

            is Command.NavigateToAndClearCommand -> {
                val destinationId = command.screen.toDestinationId()
                val clearToId = command.clearToScreen.toDestinationId()

                val options = navOptions {
                    launchSingleTop = true
                    popUpTo(clearToId) {
                        inclusive = true
                    }
                }

                navController
                    ?.navigate(
                        resId = destinationId,
                        args = null,
                        navOptions = options
                    )
            }

            is Command.NavigateToCustomTabsCommand -> {
                oAuthLauncher.launchOAuth()
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
            val hasPreviousScreen = navController?.previousBackStackEntry != null
            if (currentDestination == R.id.authFragment || hasPreviousScreen.not()) {
                activity?.finish()
            } else {
                navController?.popBackStack()
            }
        }
    }
}