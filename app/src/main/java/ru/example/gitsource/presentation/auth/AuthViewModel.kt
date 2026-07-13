package ru.example.gitsource.presentation.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.LoginClicked -> handleLoginClick()
            is AuthAction.AuthCodeReceived -> handleGitHubAuthCode(action.code)
        }
    }

    private fun handleLoginClick() {
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
                    _events.emit(AuthEvent.ShowError(networkError))
                }
            )
        }
    }
}