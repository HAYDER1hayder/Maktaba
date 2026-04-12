package com.ElOuedUniv.maktaba.presentation.book.add

data class AddBookUiState(
    val title: String = "",
    val isbn: String = "",
    val nbPages: String = "",
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    // ✅ الإضافة الجديدة: للتحكم في تفعيل زر الحفظ
    val isButtonEnabled: Boolean = false
)