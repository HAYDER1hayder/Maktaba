package com.ElOuedUniv.maktaba.presentation.book

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * UI State for the Book list screen.
 * تم التعديل ليتوافق مع التصميم الفخم (VIP Design) بنسبة 100%
 */
data class BookUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAddingBook: Boolean = false, // الحالة الخاصة بظهور نموذج الإضافة

    // --- إضافات لمطابقة الهدف في الصورة ---
    val searchQuery: String = "", // للبحث الذي يظهر في أعلى الصورة المستهدفة
    val totalBooksCount: Int = 0   // لعرض إحصائية "Books in vault" كما في التصميم
)