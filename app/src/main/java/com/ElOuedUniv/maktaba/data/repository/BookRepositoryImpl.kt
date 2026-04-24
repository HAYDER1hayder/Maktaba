package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.di.SupabaseModule
import com.ElOuedUniv.maktaba.data.model.Book
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor() : BookRepository {

    // نستخدم الكلاينت الذي قمنا بتعريفه في SupabaseModule
    private val supabase = SupabaseModule.client

    override fun getAllBooks(): Flow<List<Book>> = flow {
        try {
            // جلب البيانات من جدول "books" في Supabase
            // وتمريرها مباشرة كـ List<Book> بفضل @Serializable
            val books = supabase.from("books")
                .select()
                .decodeList<Book>()

            emit(books)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // نرجع قائمة فارغة في حال حدوث خطأ
        }
    }.flowOn(Dispatchers.IO) // التأكد من أن العملية تتم في الخلفية

    override fun getBookByIsbn(isbn: String): Book? {
        // ملاحظة: في الـ Real Backend، يفضل جلب الكتاب بشكل Suspend
        // لكن للتبسيط حالياً، يمكنك تركه أو تنفيذ استعلام مباشر
        return null
    }

    override suspend fun addBook(book: Book) {
        try {
            // إضافة الكتاب أو تحديثه في Supabase (Upsert)
            supabase.from("books").upsert(book)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}