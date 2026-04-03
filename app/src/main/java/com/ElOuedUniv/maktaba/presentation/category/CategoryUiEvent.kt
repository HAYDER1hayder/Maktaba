package com.ElOuedUniv.maktaba.presentation.category

/**
 * One-time UI events (System events) for the Category screen.
 * تم إكمال الأحداث لدعم التنبيهات، الرجوع، والانتقال للفئات.
 */
sealed interface CategoryUiEvent {
    // لإظهار رسالة خطأ أو تنبيه للمستخدم بشكل مفاجئ
    data class ShowSnackbar(val message: String) : CategoryUiEvent

    // لإغلاق الشاشة والرجوع للخلف
    object NavigateBack : CategoryUiEvent

    // الحدث الجديد: الانتقال لشاشة الكتب بناءً على التصنيف المختار
    data class NavigateToBookList(val categoryId: String) : CategoryUiEvent
}

sealed class BookDetailUiEvent {
    object NavigateBack : BookDetailUiEvent()
}