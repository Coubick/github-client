package ru.example.gitsource.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.example.gitsource.domain.popular.PopularRepositoriesAction
import ru.example.gitsource.domain.popular.PopularRepositoriesUiState
import ru.example.gitsource.domain.popular.RepositorySearchService
import javax.inject.Inject

@HiltViewModel
internal class PopularRepositoriesViewModel @Inject constructor(
    private val repositorySearchService: RepositorySearchService
) : ViewModel() {
//    private val repositoriesList
//    private val state: PopularRepositoriesUiState

    init {
        viewModelScope.launch {
            repositorySearchService.searchRepositories()
        }
    }

    fun onAction(action: PopularRepositoriesAction) {
        when (action) {
            is PopularRepositoriesAction.RepositoryCardClicked -> onRepositoryCardClicked()
            is PopularRepositoriesAction.SearchRepositoriesClicked -> onSearchRepositoryClicked()
            is PopularRepositoriesAction.LogoutClicked -> onLogoutClicked()
        }
    }

    private fun onRepositoryCardClicked() {

    }

    private fun onLogoutClicked() {

    }

    private fun onSearchRepositoryClicked() {

    }
}