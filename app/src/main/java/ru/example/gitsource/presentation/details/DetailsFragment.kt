package ru.example.gitsource.presentation.details

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
import ru.example.gitsource.presentation.navigation.Command
import ru.example.gitsource.presentation.navigation.Navigator
import ru.example.gitsource.theme.GitSourceTheme
import javax.inject.Inject

@AndroidEntryPoint
internal class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

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
                    DetailsScreen(
                        state = state,
                        onAction = { action -> viewModel.onAction(action) }
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
                    is DetailsEvent.NavigateBack -> {
                        val command = Command.BackCommand
                        navigator.execute(command)
                    }

                    is DetailsEvent.ShowError -> {
                        Toast.makeText(
                            requireContext(),
                            event.errorMessageId,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }
}