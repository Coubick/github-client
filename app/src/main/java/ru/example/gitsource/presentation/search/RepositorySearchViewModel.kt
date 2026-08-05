package ru.example.gitsource.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.popular.RepoRepository
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.auth.ErrorMapper
import javax.inject.Inject

@HiltViewModel
internal class RepositorySearchViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
    }

    private val _query = MutableStateFlow("")

    private val _state = MutableStateFlow(
        RepositorySearchState(
            isLoading = false,
            isFound = false,
            repositoriesList = emptyList(),
            searchRequestText = "",
        )
    )
    private val _event = MutableSharedFlow<RepositorySearchEvent>()

    val state = _state.asStateFlow()
    val event = _event.asSharedFlow()

    init {
        onUpdateSearchResult()
    }

    fun onAction(action: RepositorySearchAction) {
        when (action) {
            is RepositorySearchAction.RepositoryCardClicked -> {
                onRepositoryClicked(action.repository)
            }

            is RepositorySearchAction.NavigateBack -> {
                onBackClicked()
            }

            is RepositorySearchAction.RepoNameEntered -> {
                onRepoNameEntered(action.repoName)
            }
        }
    }

    private fun onRepoNameEntered(repoName: String) {
        _state.update { repositorySearchState ->
            repositorySearchState.copy(
                isLoading = true,
                searchRequestText = repoName,
                repositoriesList = emptyList()
            )
        }

        _query.value = repoName
    }

    private fun onRepositoryClicked(repository: RepositoryCardEntity) {
        viewModelScope.launch {
            _event.emit(
                RepositorySearchEvent.NavigateToRepositoryCard(repository)
            )
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            _event.emit(
                RepositorySearchEvent.NavigateBack
            )
        }
    }

    private fun onUpdateSearchResult() {
        _query.debounce(DELAY)
            .distinctUntilChanged()
            .flatMapLatest { repoName ->
                flow {
                    if (repoName.isNotBlank()) {
                        val result = repoRepository.getRepositoriesByName(repoName)
                        emit(result)
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .onEach { searchRepositoriesResult ->
                searchRepositoriesResult.fold(
                    onSuccess = {
                        val entityRepositories = searchRepositoriesResult.getOrNull()
                        if (entityRepositories != null) {
                            _state.update { repositorySearchState ->
                                repositorySearchState.copy(
                                    repositoriesList = entityRepositories,
                                    isLoading = false,
                                    isFound = true
                                )
                            }
                        } else {
                            _state.update { repositorySearchState ->
                                repositorySearchState.copy(
                                    isLoading = false,
                                    isFound = false
                                )
                            }
                            val resId = ErrorMapper.mapToStringMessage(NetworkError.Unknown)
                            _event.emit(RepositorySearchEvent.ShowError(resId))
                        }
                    },
                    onFailure = { error ->
                        _state.update { repositorySearchState ->
                            repositorySearchState.copy(
                                isLoading = false,
                                isFound = false
                            )
                        }
                        val resId = ErrorMapper.mapToStringMessage(error)
                        _event.emit(RepositorySearchEvent.ShowError(resId))
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}