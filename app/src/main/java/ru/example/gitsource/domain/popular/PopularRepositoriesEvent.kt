package ru.example.gitsource.domain.popular


internal sealed interface PopularRepositoriesEvent {
    data object NavigateToRepositoriesSearch
    data object NavigateBack
}