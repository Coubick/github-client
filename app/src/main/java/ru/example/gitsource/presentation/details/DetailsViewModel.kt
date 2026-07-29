package ru.example.gitsource.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkError
import ru.example.gitsource.domain.details.DetailsAction
import ru.example.gitsource.domain.details.DetailsEvent
import ru.example.gitsource.domain.details.DetailsRepository
import ru.example.gitsource.domain.details.DetailsUiState
import ru.example.gitsource.presentation.auth.ErrorMapper
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(
        DetailsUiState(
            isLoading = false,
            details = null
        )
    )
    private val _event = MutableSharedFlow<DetailsEvent>()

    private companion object {
        const val REPOSITORY_OWNER_NAME_KEY = "repositoryOwnerName"
        const val REPOSITORY_NAME_KEY = "repositoryName"
    }

    val state = _state.asStateFlow()
    val event = _event.asSharedFlow()

    init {
        getDetails()
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.NavigateBackClicked -> {
                viewModelScope.launch {
                    _event.emit(DetailsEvent.NavigateBack)
                }
            }
        }
    }

    private fun getDetails() {
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
                        if (repositoryDetails != null) {
                            _state.update { detailsUiState ->
                                detailsUiState.copy(
                                    details = repositoryDetails
                                )
                            }
                        } else {
                            val resId =
                                ErrorMapper.mapToStringMessage(NetworkError.EmptyResponseBody)
                            _event.emit(DetailsEvent.ShowError(resId))
                        }
                    },

                    onFailure = { error ->
                        val resId = ErrorMapper.mapToStringMessage(error)
                        _event.emit(DetailsEvent.ShowError(resId))
                    }
                )

            } else {
                val resId = ErrorMapper.mapToStringMessage(NetworkError.EmptyResponseBody)
                _event.emit(
                    DetailsEvent.ShowError(resId)
                )
            }
        }
    }
}