package ru.example.gitsource.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.gitsource.presentation.Screen
import ru.example.gitsource.presentation.navigation.Command
import ru.example.gitsource.presentation.navigation.Navigator
import ru.example.gitsource.theme.GitSourceTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class AuthFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                GitSourceTheme {
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    AuthScreen(
                        state = state,
                        onLoginClick = {
                            viewModel.onAction(AuthAction.LoginClicked)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is AuthEvent.LaunchAuth -> {
                        navigator.execute(Command.NavigateToCustomTabsCommand)
                    }

                    is AuthEvent.NavigateToPopular -> {
                        val command = Command.NavigateToCommand(Screen.PopularRepositoriesScreen)
                        navigator.execute(command)
                    }

                    is AuthEvent.ShowError -> {
                        Toast.makeText(
                            requireContext(),
                            event.errorMessageResId,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAction(AuthAction.LoginCancelled)
    }
}