package ru.example.gitsource.presentation

import ru.example.gitsource.R

sealed interface Screen {

    sealed interface Internal : Screen {
        val destinationId: Int
    }

    data object StartScreen : Internal {
        override val destinationId: Int = R.id.startFragment
    }

    data object AuthScreen : Internal {
        override val destinationId: Int = R.id.authFragment
    }

    data object PopularRepositoriesScreen : Internal {
        override val destinationId: Int = R.id.popularRepositoriesFragment
    }

    data class RepositoryDetailsScreen(
        val repositoryName: String,
        val repositoryOwnerName: String
    ) : Internal {
        override val destinationId: Int = R.id.repositoryDetailsFragment
    }

    data object SearchRepositoryScreen : Internal {
        override val destinationId: Int = R.id.searchRepositoryFragment
    }

    data object OAuthScreen : Screen
}