package ru.example.gitsource.presentation.search

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
import ru.example.gitsource.presentation.Screen.RepositoryDetailsScreen
import ru.example.gitsource.presentation.navigation.Command
import ru.example.gitsource.presentation.navigation.Command.NavigateToCommand
import ru.example.gitsource.presentation.navigation.Navigator
import ru.example.gitsource.theme.GitSourceTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class RepositorySearchFragment : Fragment(){

    private val viewModel: RepositorySearchViewModel by viewModels()

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
                    RepositorySearchScreen(
                        onAction = { action -> viewModel.onAction(action) },
                        onLoadNextPage = { viewModel.loadNextPage() },
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
                    is RepositorySearchEvent.NavigateBack -> {
                        val command = Command.BackCommand
                        navigator.execute(command)
                    }

                    is RepositorySearchEvent.NavigateToRepositoryCard -> {
                        val command = NavigateToCommand(
                            RepositoryDetailsScreen(
                                repositoryName = event.repository.name,
                                repositoryOwnerName = event.repository.authorName
                            )
                        )
                        navigator.execute(command)
                    }

                    is RepositorySearchEvent.ShowError -> {
                        Toast.makeText(
                            requireContext(),
                            event.error,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }
}