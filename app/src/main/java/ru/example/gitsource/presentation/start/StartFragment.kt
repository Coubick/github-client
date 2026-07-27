package ru.example.gitsource.presentation.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.gitsource.domain.LoginStatusChecker
import ru.example.gitsource.presentation.navigation.Command
import ru.example.gitsource.presentation.navigation.Navigator
import ru.example.gitsource.presentation.Screen
import javax.inject.Inject

@AndroidEntryPoint
internal class StartFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loginStatusChecker: LoginStatusChecker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                MaterialTheme {
                    StartScreen()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val isLoggedIn = loginStatusChecker.isLoggedIn()
            if (isLoggedIn) {
                navigator.execute(
                    Command.NavigateToAndClearCommand(
                        screen = Screen.PopularRepositoriesScreen,
                        clearToScreen = Screen.StartScreen,
                    )
                )
            } else {
                navigator.execute(
                    Command.NavigateToAndClearCommand(
                        screen = Screen.AuthScreen,
                        clearToScreen = Screen.StartScreen
                    )
                )
            }
        }
    }
}