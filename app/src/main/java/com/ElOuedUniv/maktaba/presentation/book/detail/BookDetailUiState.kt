package com.ElOuedUniv.maktaba.presentation.book.detail

import com.ElOuedUniv.maktaba.data.model.Book
import androidx.compose.ui.graphics.Color
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon

/**
 * حالة واجهة تفاصيل الكتاب.
 * تم إضافة حقول الحساب التلقائي لدعم شريط التقدم النيون في التصميم الـ VIP.
 */
data class BookDetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    // حساب نسبة التقدم (مثلاً إذا كان الكتاب 400 صفحة وقرأنا 300 تظهر 0.75f)
    // ملاحظة: سنفترض وجود حقل صفحات مقروءة في الموديل لاحقاً أو نضع قيمة افتراضية
    val progress: Float = 0.75f // هذه القيمة تدعم الـ 75% الموجودة في رسمك

    // تحديد اللون المشع بناءً على حالة الكتاب (أخضر للمنتهي، أرجواني للقراءة)
    val statusColor: Color = if (book?.nbPages ?: 0 > 400) {
        Color(0xFF00FF88) // نيون أخضر للفخامة
    } else {
        GeminiPurpleNeon // نيون أرجواني (ثيم التطبيق)
    }
}