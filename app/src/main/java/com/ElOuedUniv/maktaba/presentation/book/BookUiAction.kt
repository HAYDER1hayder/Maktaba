package com.ElOuedUniv.maktaba.presentation.book

/**
 * UI Actions representing user interactions on the Book screen.
 * تم التحديث لدعم التفاعل الفخم (VIP Interactions)
 */
sealed interface BookUiAction {
    object RefreshBooks : BookUiAction
    object OnAddBookClick : BookUiAction
    object OnDismissAddBook : BookUiAction

    // الأكشن الخاص بتأكيد الإضافة من النموذج
    data class OnAddBookConfirm(val title: String, val isbn: String, val nbPages: Int) : BookUiAction

    // --- إضافات حاسمة للتطابق مع الهدف ---

    // عند الضغط على كتاب معين لفتح تفاصيله (ضروري للملاحة)
    data class OnBookClick(val isbn: String) : BookUiAction

    // عند تغيير نص البحث في الأعلى (لمطابقة شريط البحث في التصميم)
    data class OnSearchQueryChange(val query: String) : BookUiAction
}