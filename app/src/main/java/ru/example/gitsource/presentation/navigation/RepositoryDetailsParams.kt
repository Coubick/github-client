package ru.example.gitsource.presentation.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import ru.example.gitsource.presentation.Screen

internal object RepositoryDetailsParams {

    private const val REPOSITORY_NAME_KEY = "repositoryName"
    private const val REPOSITORY_OWNER_NAME_KEY = "repositoryOwnerName"


    fun makeBundle(screen: Screen): Bundle? {
        val bundle = when (val screen = screen) {
            is Screen.RepositoryDetailsScreen -> {
                bundleOf(
                    REPOSITORY_NAME_KEY to screen.repositoryName,
                    REPOSITORY_OWNER_NAME_KEY to screen.repositoryOwnerName
                )
            }

            else -> {
                null
            }
        }

        return bundle
    }

    fun getRepositoryName(savedStateHandle: SavedStateHandle) : String? {
        return savedStateHandle.get<String>(REPOSITORY_NAME_KEY)
    }

    fun getRepositoryOwnerName(savedStateHandle: SavedStateHandle) : String? {
        return savedStateHandle.get<String>(REPOSITORY_OWNER_NAME_KEY)
    }
}