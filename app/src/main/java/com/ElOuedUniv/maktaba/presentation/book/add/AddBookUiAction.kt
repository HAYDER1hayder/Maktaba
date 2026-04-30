package com.ElOuedUniv.maktaba.presentation.book.add

sealed class AddBookUiAction {
    data class OnTitleChange(val title: String) : AddBookUiAction()
    data class OnIsbnChange(val isbn: String) : AddBookUiAction()

    // في ملف AddBookUiAction.kt
    data class OnPagesChange(val pages: String) : AddBookUiAction()
    data class OnPagesReadChange(val pagesRead: String) : AddBookUiAction() // الأكشن الجديد

    data class OnImageSelected(val uri: String?, val imageBytes: ByteArray? = null) : AddBookUiAction()

    object OnRemoveImage : AddBookUiAction()
    object OnAddClick : AddBookUiAction()
}
