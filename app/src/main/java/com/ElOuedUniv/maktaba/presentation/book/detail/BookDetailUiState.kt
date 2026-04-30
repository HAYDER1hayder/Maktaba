package com.ElOuedUniv.maktaba.presentation.book.detail

import com.ElOuedUniv.maktaba.data.model.Book
import androidx.compose.ui.graphics.Color
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon

data class BookDetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    // ✅ التقدم الآن يُحسب ديناميكياً من الموديل
    val progress: Float = book?.progress ?: 0f

    // ✅ استخراج القيم لعرضها في العدادات الرقمية
    val pagesRead: Int = book?.pagesRead ?: 0
    val totalPages: Int = book?.nbPages ?: 0
    val pagesRemaining: Int = book?.pagesRemaining ?: 0

    // ✅ منطق الألوان النيون المطور (Cyber Colors)
    val statusColor: Color = when {
        progress >= 1f -> Color(0xFF00FF88) // أخضر نيون (مكتمل)
        progress > 0.5f -> GeminiPurpleNeon // أرجواني نيون (متقدم)
        else -> Color(0xFF00E5FF) // أزرق سيان نيون (بداية القراءة)
    }
}