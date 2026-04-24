package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Book(
    val isbn: String,
    val title: String,
    val nbPages: Int, // الإجمالي
    val pagesRead: Int = 0, // الجديد: الصفحات التي تمت قراءتها
    val imageUrl: String? = null,
    val status: String = "Reading"
) {
    // ميزة مجنونة: حساب النسبة المئوية تلقائياً داخل الـ Model
    @Transient
    val progress: Float
        get() = if (nbPages > 0) pagesRead.toFloat() / nbPages.toFloat() else 0f


    // ميزة مجنونة 2: حساب الصفحات المتبقية
    @Transient
    val pagesRemaining: Int
        get() = (nbPages - pagesRead).coerceAtLeast(0)
}