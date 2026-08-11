package ru.example.gitsource.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.popular.RepoRepository
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.auth.ErrorMapper
import ru.example.gitsource.presentation.paging.AppPaginator
import ru.example.gitsource.presentation.paging.Paginator
import javax.inject.Inject

@HiltViewModel
internal class RepositorySearchViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
    }

    private val _query = MutableStateFlow("")

    private val paginator: Paginator<RepositoryCardEntity> = AppPaginator(
        scope = viewModelScope,
        loadPage = { page ->
            val currentSearchQuery = _query.value

            repoRepository.getRepositoriesByName(
                page = page,
                repositoryName = currentSearchQuery,
            )
        }
    )

    private val _state = MutableStateFlow(
        RepositorySearchState(
            isLoading = false,
            isFound = false,
            paginator = paginator,
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
            .onEach {
                _state.update { repositorySearchState ->
                    repositorySearchState.copy(
                        isLoading = true,
                        isFound = false
                    )
                }

                paginator.restart()
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            paginator.state.collect { pagingState ->

                _state.update { repositorySearchState ->
                    repositorySearchState.copy(
                        isFound = !pagingState.isLoading && pagingState.items.isNotEmpty()
                    )
                }

                if (!pagingState.isLoading) {
                    if (pagingState.error == null) {
                        if (pagingState.items.isEmpty() && _query.value.isNotBlank()) {
                            val resId =
                                ErrorMapper.mapToStringMessage(NetworkError.EmptyResponseBody)
                            _event.emit(RepositorySearchEvent.ShowError(resId))
                        }

                    } else {
                        val resId =
                            ErrorMapper.mapToStringMessage(NetworkError.LoadingError(pagingState.error))
                        _event.emit(RepositorySearchEvent.ShowError(resId))
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        paginator.loadNext()
    }
}