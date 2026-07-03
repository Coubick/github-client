package ru.example.gitsource.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gitsource.R
import ru.example.gitsource.navigation.BackCommand
import ru.example.gitsource.navigation.NavigateToCommand
import ru.example.gitsource.navigation.Navigator
import javax.inject.Inject

@AndroidEntryPoint
internal class AuthFragment : Fragment(R.layout.fragment_auth) {

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.login_button).setOnClickListener {
            val command = NavigateToCommand(
                R.id.popularRepositoriesFragment,
                navController = findNavController(),
            )
            navigator.executeCommand(command)
        }

        view.findViewById<View>(R.id.back_button).setOnClickListener {
            val command = BackCommand(requireActivity().onBackPressedDispatcher)
            navigator.executeCommand(command)
        }
    }
}