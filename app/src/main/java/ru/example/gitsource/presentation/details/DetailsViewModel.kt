package ru.example.gitsource.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.details.DetailsRepository
import ru.example.gitsource.presentation.auth.ErrorMapper
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        const val REPOSITORY_OWNER_NAME_KEY = "repositoryOwnerName"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }

    private val _state = MutableStateFlow(
        DetailsUiState(
            isLoading = false,
            details = null
        )
    )

    private val _event = MutableSharedFlow<DetailsEvent>()

    val state = _state.asStateFlow()
    val event = _event.asSharedFlow()

    init {
        onAction(DetailsAction.LoadDetails)
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.NavigateBackClicked -> {
                viewModelScope.launch {
                    _event.emit(DetailsEvent.NavigateBack)
                }
            }

            is DetailsAction.LoadDetails -> {
                getDetails()
            }
        }
    }

    private fun getDetails() {
        _state.update { uiState -> uiState.copy(isLoading = true) }
        viewModelScope.launch {
            val repositoryName = savedStateHandle.get<String>(REPOSITORY_NAME_KEY)
            val ownerName = savedStateHandle.get<String>(REPOSITORY_OWNER_NAME_KEY)

            if (ownerName != null && repositoryName != null) {
                val result = detailsRepository.getRepositoryDetails(
                    ownerName = ownerName,
                    repoName = repositoryName
                )
                result.fold(
                    onSuccess = {
                        val repositoryDetails = result.getOrNull()
                        _state.update { uiState -> uiState.copy(isLoading = false) }
                        if (repositoryDetails != null) {
                                _state.update { detailsUiState ->
                                    detailsUiState.copy(
                                        details = repositoryDetails
                                    )
                                }
                        } else {
                            handleEmptyResult()
                        }
                    },

                    onFailure = { error ->
                        _state.update { uiState -> uiState.copy(isLoading = false) }
                        withContext(Dispatchers.Default) {
                            val resId = ErrorMapper.mapToStringMessage(error)
                            _event.emit(DetailsEvent.ShowError(resId))
                        }
                    }
                )

            } else {
                handleEmptyResult()
            }
        }
    }

    private suspend fun handleEmptyResult(){
        _state.update { uiState -> uiState.copy(isLoading = false) }
        val resId = ErrorMapper.mapToStringMessage(NetworkError.EmptyResponseBody)
        _event.emit(
            DetailsEvent.ShowError(resId)
        )
    }
}