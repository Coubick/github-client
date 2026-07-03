package ru.example.gitsource.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gitsource.R
import ru.example.gitsource.navigation.BackCommand
import ru.example.gitsource.navigation.Navigator
import javax.inject.Inject

@AndroidEntryPoint
internal class PopularRepositoriesFragment : Fragment(R.layout.fragment_popular_repository) {
    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.back_button).setOnClickListener {
            val command = BackCommand(
                onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
            )
            navigator.executeCommand(command)
        }
    }
}