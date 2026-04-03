package com.ElOuedUniv.maktaba.presentation.book.detail

/**
 * الأفعال التي يقوم بها المستخدم في شاشة تفاصيل الكتاب.
 * تم إضافة خيارات التعديل والحذف لتتوافق مع التصميم الاحترافي.
 */
sealed class BookDetailUiAction {
    // عند الضغط على زر الرجوع في الأعلى
    object OnBackClick : BookDetailUiAction()

    // عند الضغط على زر التعديل (القلم في صورتك)
    object OnEditClick : BookDetailUiAction()

    // عند الضغط على زر الحذف (سلة المهملات)
    object OnDeleteClick : BookDetailUiAction()

    // فعل إضافي: لتحديث حالة الكتاب (Reading / Finished)
    object OnToggleStatus : BookDetailUiAction()
}