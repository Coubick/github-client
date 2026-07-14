package ru.example.gitsource.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.gitsource.domain.AuthAction
import ru.example.gitsource.domain.AuthEvent
import ru.example.gitsource.domain.OAuthLauncher
import ru.example.gitsource.navigation.Command
import ru.example.gitsource.navigation.Navigator
import ru.example.gitsource.presentation.Screen
import javax.inject.Inject

@AndroidEntryPoint
internal class AuthFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var oAuthLauncher: OAuthLauncher

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
                MaterialTheme {
                    AuthScreen(
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

        val code = arguments?.getString("code")

        if (!code.isNullOrEmpty()) {
            viewModel.onAction(AuthAction.AuthCodeReceived(code))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is AuthEvent.LaunchAuth -> {
                        oAuthLauncher.launchAuth(requireContext())
                    }

                    is AuthEvent.NavigateToPopular -> {
                        val command = Command.NavigateToCommand(Screen.PopularRepositoriesScreen)
                        navigator.execute(command)
                    }

                    is AuthEvent.ShowError -> {
                        val message = ErrorMapper.mapToStringMessage(
                            requireContext(),
                            event.error
                        )
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}