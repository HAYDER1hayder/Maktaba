package com.ElOuedUniv.maktaba.presentation.book.detail

sealed interface BookDetailUiEvent {

    object NavigateBack : BookDetailUiEvent

    data class NavigateToEdit(val isbn: String) : BookDetailUiEvent

    data class ShowSnackbar(val message: String) : BookDetailUiEvent
}