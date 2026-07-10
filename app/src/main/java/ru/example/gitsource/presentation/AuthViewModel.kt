package ru.example.gitsource.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.gitsource.BuildConfig
import ru.example.gitsource.R
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.AuthAction
import ru.example.gitsource.domain.AuthEvent
import ru.example.gitsource.domain.AuthRepository
import ru.example.gitsource.domain.AuthUiState
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

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
            _events.emit(AuthEvent.LaunchAuth(BuildConfig.GITHUB_CLIENT_ID))
        }
    }

    private fun handleGitHubAuthCode(code: String) {
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)

            val result = repository.login(
                clientId = BuildConfig.GITHUB_CLIENT_ID,
                clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
                code = code
            )

            result.fold(
                onSuccess = {
                    _state.value = AuthUiState(isLoading = false)
                    _events.emit(AuthEvent.NavigateToPopular)
                },
                onFailure = { error ->
                    _state.value = AuthUiState(isLoading = false)
                    val message = if (error is NetworkError) {
                        ErrorMapper.mapToStringMessage(context, error)
                    } else {
                        context.getString(R.string.error_unknown)
                    }

                    _events.emit(AuthEvent.ShowError(message))
                }
            )
        }
    }
}