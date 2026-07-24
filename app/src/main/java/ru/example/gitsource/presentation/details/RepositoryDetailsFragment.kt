package ru.example.gitsource.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.example.gitsource.theme.GitSourceTheme

internal class RepositoryDetailsFragment : Fragment() {
    // TODO https://jira.rutube.ru/browse/MOBAPP-18311
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GitSourceTheme {
                    RepositoryDetailsScreen()
                }
            }
        }
    }
}