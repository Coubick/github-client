package ru.example.gitsource.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
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
internal class PopularRepositoriesFragment : Fragment() {
    private val viewModel: PopularRepositoriesViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GitSourceTheme {
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    PopularRepositoriesScreen(
                        onAction = { action -> viewModel.onAction(action) },
                        state = state,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {
                    is PopularRepositoriesEvent.NavigateToRepositoryCard -> {
                        val command =
                            Command.NavigateToCommand(
                                Screen.RepositoryDetailsScreen(
                                    repositoryName = event.repository.name,
                                    repositoryOwnerName = event.repository.authorName
                                )
                            )
                        navigator.execute(command)
                    }

                    is PopularRepositoriesEvent.NavigateToRepositoriesSearch -> {
                        val command = Command.NavigateToCommand(Screen.SearchRepositoryScreen)
                        navigator.execute(command)
                    }

                    is PopularRepositoriesEvent.ShowError -> {
                        Toast.makeText(
                            requireContext(),
                            event.errorMessageResId,
                            Toast.LENGTH_LONG
                        )
                    }

                    is PopularRepositoriesEvent.NavigateToAuth -> {
                        val command = Command.NavigateToCommand(Screen.AuthScreen)
                        navigator.execute(command)
                    }
                }
            }
        }
    }
}