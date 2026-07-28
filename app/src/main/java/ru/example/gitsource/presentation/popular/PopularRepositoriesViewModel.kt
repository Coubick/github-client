package ru.example.gitsource.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.auth.AuthRepository
import ru.example.gitsource.domain.popular.RepoRepository
import ru.example.gitsource.domain.popular.RepositoryCardEntity
import ru.example.gitsource.presentation.auth.ErrorMapper
import javax.inject.Inject

@HiltViewModel
internal class PopularRepositoriesViewModel @Inject constructor(
    private val repositoryLoadService: RepoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        PopularRepositoriesUiState(
            isLoading = false,
            repositoriesList = emptyList(),
            isLogoutDialogVisible = false
        )
    )
    private val _event = MutableSharedFlow<PopularRepositoriesEvent>()

    val state: StateFlow<PopularRepositoriesUiState> = _state.asStateFlow()
    val event: SharedFlow<PopularRepositoriesEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            loadRepositories()
        }
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
        viewModelScope.launch {
            _state.update { popularRepositoriesUiState ->
                popularRepositoriesUiState.copy(
                    isLogoutDialogVisible = true
                )
            }
        }
    }

    private fun onSearchRepositoryClicked() {
        viewModelScope.launch {
            _event.emit(PopularRepositoriesEvent.NavigateToRepositoriesSearch)
        }
    }

    private fun loadRepositories() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = repositoryLoadService.getRepositories()
            result.fold(
                onSuccess = {
                    val entityRepositories = result.getOrNull()
                    if (entityRepositories != null) {
                        _state.update { popularRepositoriesUiState ->
                            popularRepositoriesUiState.copy(
                                repositoriesList = entityRepositories,
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        val resId = ErrorMapper.mapToStringMessage(NetworkError.Unknown)
                        _event.emit(PopularRepositoriesEvent.ShowError(resId))
                    }
                },

                onFailure = { error ->
                    _state.update { it.copy(isLoading = false) }
                    val resId = ErrorMapper.mapToStringMessage(error)
                    _event.emit(PopularRepositoriesEvent.ShowError(resId))
                }
            )
        }
    }

    private fun onLogoutConfirmed() {
        viewModelScope.launch {
            _state.update { it.copy(isLogoutDialogVisible = false) }

            val result = authRepository.logout()
            result.fold(
                onSuccess = {
                    _event.emit(PopularRepositoriesEvent.NavigateToAuth)
                },
                onFailure = { error ->
                    withContext(Dispatchers.IO) {
                        val resId = ErrorMapper.mapToStringMessage(error)
                        _event.emit(PopularRepositoriesEvent.ShowError(resId))
                    }
                }
            )
        }
    }

    private fun onLogoutDismissed() {
        _state.update { popularRepositoriesUiState ->
            popularRepositoriesUiState.copy(isLogoutDialogVisible = false)
        }
    }
}