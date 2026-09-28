package ru.example.gitsource.presentation.paging

import com.jamal_aliev.paginator.core.page.PaginatorUiState
import com.jamal_aliev.paginator.offset.dsl.mutablePaginator
import com.jamal_aliev.paginator.offset.extension.uiState
import com.jamal_aliev.paginator.offset.load.LoadResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.example.gitsource.data.network.NetworkConstants.PER_PAGE
import javax.inject.Inject

internal class AppPaginator<T> @Inject constructor(
    private val scope: CoroutineScope,
    private val loadPage: suspend (page: Int) -> Result<List<T>>,
) : Paginator<T> {

    private val _state = MutableStateFlow(
        PagingState<T>(
            items = emptyList(),
            isLoading = false,
            error = null,
            endOfPaginationReached = false
        )
    )
    override val state: StateFlow<PagingState<T>> = _state.asStateFlow()

    private val appPaginator = mutablePaginator<T> {
        load { page ->
            val result = loadPage(page)

            result.fold(
                onSuccess = {
                    val items = result.getOrNull() ?: emptyList()
                    if (items.size < PER_PAGE) {
                        this.finalPage = page
                    }

                    LoadResult(items)
                },
                onFailure = { error ->
                    throw result.exceptionOrNull() ?: RuntimeException(error)
                }
            )
        }
    }

    init {
        scope.launch {
            appPaginator.uiState.collect { libraryUiState ->
                _state.value = when (libraryUiState) {
                    is PaginatorUiState.Idle -> PagingState(
                        items = emptyList(),
                        isLoading = false,
                        error = null,
                        endOfPaginationReached = false
                    )

                    is PaginatorUiState.Loading -> PagingState(
                        items = emptyList(),
                        isLoading = true,
                        error = null,
                        endOfPaginationReached = false
                    )

                    is PaginatorUiState.Empty -> PagingState(
                        items = emptyList(),
                        isLoading = false,
                        error = null,
                        endOfPaginationReached = true
                    )

                    is PaginatorUiState.Error -> PagingState(
                        items = _state.value.items,
                        isLoading = false,
                        error = libraryUiState.state.exception,
                        endOfPaginationReached = _state.value.endOfPaginationReached
                    )

                    is PaginatorUiState.Content -> {
                        val mappedItems = libraryUiState.items.map { it }
                        val isPageLoading = libraryUiState.appendState != null

                        PagingState(
                            items = mappedItems,
                            isLoading = isPageLoading,
                            error = null,
                            endOfPaginationReached = (mappedItems.size % PER_PAGE != 0) || mappedItems.isEmpty()
                        )
                    }
                }
            }
        }
    }

    override fun loadNext() {
        if (_state.value.endOfPaginationReached || _state.value.isLoading) return
        scope.launch {
            appPaginator.goNextPage()
        }
    }

    override fun restart() {
        scope.launch {
            appPaginator.restart()
        }
    }
}