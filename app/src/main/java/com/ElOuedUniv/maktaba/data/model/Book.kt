package com.ElOuedUniv.maktaba.data.model

data class Book(
    val isbn: String,
    val title: String,
    val nbPages: Int, // الإجمالي
    val pagesRead: Int = 0, // الجديد: الصفحات التي تمت قراءتها
    val imageUrl: String? = null,
    val status: String = "Reading"
) {
    // ميزة مجنونة: حساب النسبة المئوية تلقائياً داخل الـ Model
    val progress: Float
        get() = if (nbPages > 0) pagesRead.toFloat() / nbPages.toFloat() else 0f

    // ميزة مجنونة 2: حساب الصفحات المتبقية
    val pagesRemaining: Int
        get() = (nbPages - pagesRead).coerceAtLeast(0)
}