package com.ElOuedUniv.maktaba.presentation.category

//UI Actions representing user interactions on the Category screen.

sealed interface CategoryUiAction {
    // عند سحب الشاشة للتحديث أو إعادة المحاولة
    data object RefreshCategories : CategoryUiAction

    // عند الضغط على زر الرجوع في الـ TopAppBar
    data object OnBackClick : CategoryUiAction

    // الأكشن الجديد: عند الضغط على تصنيف معين (نحتاج الـ ID لنعرف أي واحد ضغط)
    data class OnCategoryClick(val categoryId: String) : CategoryUiAction
}