package com.ElOuedUniv.maktaba.presentation.book.add

data class AddBookUiState(
    val title: String = "",
    val isbn: String = "",
    val nbPages: String = "",
    val pagesRead: String = "0", // الحقل الجديد
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val errorMessage: String? = null,
    val isButtonEnabled: Boolean = false
)
