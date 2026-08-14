package ru.example.gitsource.presentation.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import ru.example.gitsource.presentation.paging.PagingConstants.PAGINATION_THRESHOLD

@Composable
internal fun PagingScrollEffect(
    listState: LazyListState,
    isLoading: Boolean,
    endOfPaginationReached: Boolean,
    onLoadNextPage: () -> Unit,
) {
    val shouldLoadNext = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount

            if (totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsCount - PAGINATION_THRESHOLD)
        }
    }

    LaunchedEffect(shouldLoadNext.value) {
        val isPageLoadAvailable =
            shouldLoadNext.value
                    && isLoading.not()
                    && endOfPaginationReached.not()
        if (isPageLoadAvailable) {
            onLoadNextPage()
        }
    }
}