package ru.example.gitsource.presentation.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.example.gitsource.domain.popular.RepoRepository
import javax.inject.Inject

@HiltViewModel
internal class RepositorySearchViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        RepositorySearchState(
            isLoading = false,
            repositoriesList = emptyList()
        )
    )
    private val _event = MutableSharedFlow<RepositorySearchEvent>()

    val state = _state.asStateFlow()
    val event = _event.asSharedFlow()

    
}