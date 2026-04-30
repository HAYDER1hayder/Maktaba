package com.ElOuedUniv.maktaba.presentation.book.detail

sealed class BookDetailUiAction {
    object OnBackClick : BookDetailUiAction()
    object OnEditClick : BookDetailUiAction() // للقلم
    object OnDeleteClick : BookDetailUiAction()
    object OnToggleStatus : BookDetailUiAction()
    object OnCoverClick : BookDetailUiAction() // للصورة (المعرض)
}