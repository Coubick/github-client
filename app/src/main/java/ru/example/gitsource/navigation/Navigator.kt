package ru.example.gitsource.navigation

import android.content.Context
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.navOptions
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.R
import ru.example.gitsource.domain.OAuthLauncher
import ru.example.gitsource.presentation.toDestinationId
import javax.inject.Inject

@ActivityScoped
internal class Navigator @Inject constructor(
    private val oAuthLauncher: OAuthLauncher
) {
    private var navController: NavController? = null
    private lateinit var onBackPressedDispatcher: OnBackPressedDispatcher
    private var activity: AppCompatActivity? = null
    private var context: Context? = null

    fun execute(command: Command) {
        when (command) {
            Command.BackCommand -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is Command.NavigateToCommand -> {
                val navigateTo = command.screen.toDestinationId()
                navController?.navigate(navigateTo)
            }

            is Command.NavigateToAndPopUpTo -> {
                val destinationId = command.screen.toDestinationId()
                val popUpToId = command.clearUpTo.toDestinationId()

                val options = navOptions {
                    popUpTo(popUpToId) {
                        inclusive = true
                    }
                }

                navController
                    ?.navigate(
                        resId = destinationId,
                        args = null,
                        navOptions = options)
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

    fun setContext(context: Context){
        this.context = context
    }

    fun launchOAuth(){
        oAuthLauncher.launchOAuth(context!!)
    }
}