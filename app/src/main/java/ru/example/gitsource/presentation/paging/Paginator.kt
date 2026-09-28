package ru.example.gitsource.presentation.paging

import kotlinx.coroutines.flow.StateFlow

internal interface Paginator<T> {
    val state: StateFlow<PagingState<T>>
    fun loadNext()
    fun restart()
}