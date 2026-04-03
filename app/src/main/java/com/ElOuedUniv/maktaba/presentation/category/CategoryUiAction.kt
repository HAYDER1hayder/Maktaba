package com.ElOuedUniv.maktaba.presentation.category

/**
 * UI Actions representing user interactions on the Category screen.
 * تم التطوير ليدعم الضغط على البطاقات الـ VIP والتحديث.
 */
sealed interface CategoryUiAction {
    // عند سحب الشاشة للتحديث أو إعادة المحاولة
    object RefreshCategories : CategoryUiAction

    // عند الضغط على زر الرجوع في الـ TopAppBar
    object OnBackClick : CategoryUiAction

    // الأكشن الجديد: عند الضغط على تصنيف معين (نحتاج الـ ID لنعرف أي واحد ضغط)
    data class OnCategoryClick(val categoryId: String) : CategoryUiAction
}