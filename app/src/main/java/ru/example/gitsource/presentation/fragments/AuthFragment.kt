package ru.example.gitsource.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gitsource.R
import ru.example.gitsource.navigation.Command
import ru.example.gitsource.navigation.Navigator
import ru.example.gitsource.presentation.screens.AuthScreen
import javax.inject.Inject

@AndroidEntryPoint
internal class AuthFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MaterialTheme {
                    AuthScreen(
                        onLoginClick = {
                            val command = Command.NavigateToCommand(R.id.popularRepositoriesFragment)
                            navigator.execute(command)
                        }
                    )
                }
            }
        }
    }
}