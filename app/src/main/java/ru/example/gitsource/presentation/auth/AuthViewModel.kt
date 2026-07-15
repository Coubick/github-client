package ru.example.gitsource.presentation.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.AuthAction
import ru.example.gitsource.domain.AuthEvent
import ru.example.gitsource.domain.AuthRepository
import ru.example.gitsource.domain.AuthUiState
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val code = savedStateHandle.get<String>("code")

        if (!code.isNullOrEmpty()){
            handleGitHubAuthCode(code)
        }

        savedStateHandle.remove<String>("code")
    }

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.LoginClicked -> onLoginClick()
        }
    }

    private fun onLoginClick() {
        viewModelScope.launch {
            _events.emit(AuthEvent.LaunchAuth)
        }
    }

    private fun handleGitHubAuthCode(code: String) {
        viewModelScope.launch {

            val result = repository.login(code = code)

            result.fold(
                onSuccess = {
                    _state.value = AuthUiState(isLoading = false)
                    _events.emit(AuthEvent.NavigateToPopular)
                },
                onFailure = { error ->
                    _state.value = AuthUiState(isLoading = false)
                    val networkError = error as? NetworkError ?: NetworkError.Unknown
                    val resId = ErrorMapper.mapToStringMessage(networkError)
                    _events.emit(AuthEvent.ShowError(resId))
                }
            )
        }
    }
}