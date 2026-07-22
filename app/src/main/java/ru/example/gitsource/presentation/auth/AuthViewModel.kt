package ru.example.gitsource.presentation.auth

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.example.gitsource.domain.auth.AuthAction
import ru.example.gitsource.domain.auth.AuthEvent
import ru.example.gitsource.domain.auth.AuthRepository
import ru.example.gitsource.domain.auth.AuthUiState
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private companion object {
        const val KEY_AUTH_CODE = "code"
    }

    init {
        val code = savedStateHandle.get<String>(KEY_AUTH_CODE)

        if (code.isNullOrEmpty().not()) {
            onGitHubAuthCode(code)
        }

        savedStateHandle.remove<String>(KEY_AUTH_CODE)
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

    private fun onGitHubAuthCode(code: String) {
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)

            val result = repository.login(code = code)

            result.fold(
                onSuccess = {
                    _state.value = AuthUiState(isLoading = false)
                    _events.emit(AuthEvent.NavigateToPopular)
                },
                onFailure = { error ->
                    _state.value = AuthUiState(isLoading = false)
                    val resId = withContext(Dispatchers.IO) {
                         ErrorMapper.mapToStringMessage(error)
                    }

                    _events.emit(AuthEvent.ShowError(resId))
                }
            )
        }
    }
}