package com.ElOuedUniv.maktaba.presentation.book

/**
 * UI Actions representing user interactions on the Book screen.
 */
sealed interface BookUiAction {
    object RefreshBooks : BookUiAction
    object OnAddBookClick : BookUiAction
    object OnDismissAddBook : BookUiAction

    // إضافة imageUrl هنا لضمان حفظ الصورة
    data class OnAddBookConfirm(
        val title: String, 
        val isbn: String, 
        val nbPages: Int,
        val imageUrl: String? = null
    ) : BookUiAction

    data class OnBookClick(val isbn: String) : BookUiAction
    data class OnSearchQueryChange(val query: String) : BookUiAction
}
