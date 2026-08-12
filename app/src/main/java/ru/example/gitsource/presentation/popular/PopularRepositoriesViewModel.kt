package ru.example.gitsource.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.auth.AuthRepository
import ru.example.gitsource.domain.popular.RepoRepository
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.auth.ErrorMapper
import ru.example.gitsource.presentation.paging.AppPaginator
import ru.example.gitsource.presentation.paging.Paginator
import javax.inject.Inject

@HiltViewModel
internal class PopularRepositoriesViewModel @Inject constructor(
    private val repoRepository: RepoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5000L
    }
    private val _event = MutableSharedFlow<PopularRepositoriesEvent>()
    private val paginator: Paginator<RepositoryCardEntity> = AppPaginator(
        scope = viewModelScope,
        loadPage = { page ->
            repoRepository.getRepositories(page = page)
        }
    )
    private val _screenForm = MutableStateFlow(
        PopularRepositoriesScreenForm(
            isLoadingDialogVisible = false
        )
    )

    val state: StateFlow<PopularRepositoriesUiState> = combine(
        paginator.state,
        _screenForm
    ) { pagingState, form ->
        PopularRepositoriesUiState(
            items = pagingState.items,
            isLoading = pagingState.isLoading,
            error = pagingState.error,
            endOfPaginationReached = pagingState.endOfPaginationReached,
            isLogoutDialogVisible = form.isLoadingDialogVisible
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = PopularRepositoriesUiState(
            items = emptyList(),
            isLoading = false,
            error = null,
            endOfPaginationReached = false,
            isLogoutDialogVisible = false
        )
    )

    val event: SharedFlow<PopularRepositoriesEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            paginator.state.collect { pagingState ->
                if (pagingState.isLoading.not()) {
                    if (pagingState.error == null) {
                        if (pagingState.items.isEmpty()) {
                            val resId =
                                ErrorMapper.mapToStringMessage(NetworkError.EmptyResponseBody)
                            _event.emit(PopularRepositoriesEvent.ShowError(resId))
                        }
                    } else {
                        val resId = ErrorMapper.mapToStringMessage(NetworkError.LoadingError(pagingState.error))
                        _event.emit(PopularRepositoriesEvent.ShowError(resId))
                    }
                }
            }
        }

        loadNextPage()
    }

    fun onAction(action: PopularRepositoriesAction) {
        when (action) {
            is PopularRepositoriesAction.RepositoryClicked -> onRepositoryClicked(action.repository)
            is PopularRepositoriesAction.SearchRepositoriesClicked -> onSearchRepositoryClicked()
            is PopularRepositoriesAction.LogoutClicked -> onLogoutClicked()
            is PopularRepositoriesAction.LogoutConfirmed -> onLogoutConfirmed()
            is PopularRepositoriesAction.LogoutDialogDismissed -> onLogoutDismissed()
        }
    }

    private fun onRepositoryClicked(repository: RepositoryCardEntity) {
        viewModelScope.launch {
            _event.emit(
                PopularRepositoriesEvent.NavigateToRepositoryCard(repository)
            )
        }
    }

    private fun onLogoutClicked() {
        updateForm { popularRepositoriesScreenForm ->
            popularRepositoriesScreenForm.copy(
                isLoadingDialogVisible = true
            )
        }
    }

    private fun onSearchRepositoryClicked() {
        viewModelScope.launch {
            _event.emit(PopularRepositoriesEvent.NavigateToRepositoriesSearch)
        }
    }

    private fun onLogoutConfirmed() {
        updateForm { popularRepositoriesScreenForm ->
            popularRepositoriesScreenForm.copy(isLoadingDialogVisible = false)
        }

        viewModelScope.launch(Dispatchers.Default) {
            val result = authRepository.logout()
            result.fold(
                onSuccess = {
                    _event.emit(PopularRepositoriesEvent.NavigateToAuth)
                },
                onFailure = { error ->
                    val resId = ErrorMapper.mapToStringMessage(error)
                    _event.emit(PopularRepositoriesEvent.ShowError(resId))
                }
            )
        }
    }

    private fun onLogoutDismissed() {
        updateForm { popularRepositoriesScreenForm ->
            popularRepositoriesScreenForm.copy(isLoadingDialogVisible = false)
        }
    }

    private fun updateForm(transform: (PopularRepositoriesScreenForm) -> PopularRepositoriesScreenForm) {
        _screenForm.update(transform)
    }

    fun loadNextPage() {
        paginator.loadNext()
    }
}