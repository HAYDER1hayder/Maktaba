package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository // تأكد من استيراد مسار الانترفيس الصحيح لديك
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    // 1. حقن (Inject) عميل Supabase مباشرة من Hilt هنا
    private val supabaseClient: SupabaseClient
) : BookRepository {

    // 2. تم حذف المتغيرات القديمة (client, supabase, postgrest, storage) لأننا سنستخدم supabaseClient المحقون

    // 3. حافظنا على الكاش المحلي الخاص بك كما صممته
    private val _booksState = MutableStateFlow<List<Book>>(emptyList())

    override fun getAllBooks(): Flow<List<Book>> {
        return flow {
            try {
                // استخدمنا supabaseClient
                val remoteBooks = supabaseClient.from("books")
                    .select()
                    .decodeList<Book>()
                _booksState.value = remoteBooks
            } catch (e: Exception) {
                android.util.Log.e("MAKTABA_API", "Error fetching: ${e.message}")
            }

            // 💡 لمسة احترافية: استبدال collect بـ emitAll
            // هذا يمنع تعليق الكوروتين (Infinite Suspension) ويؤدي نفس الغرض الذي أردته بكفاءة أعلى
            emitAll(_booksState)

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookByIsbn(isbn: String): Book? = withContext(Dispatchers.IO) {
        try {
            supabaseClient.from("books").select {
                filter { eq("isbn", isbn) }
            }.decodeSingleOrNull<Book>()
        } catch (e: Exception) { null }
    }

    override suspend fun addBook(book: Book) = withContext(Dispatchers.IO) {
        try {
            supabaseClient.from("books").upsert(book)

            val currentList = _booksState.value.toMutableList()
            val index = currentList.indexOfFirst { it.isbn == book.isbn }
            if (index != -1) {
                currentList[index] = book
            } else {
                currentList.add(0, book)
            }
            _booksState.value = currentList
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updatePagesRead(isbn: String, newPagesRead: Int) { // 👈 لاحظ قمنا بإزالة علامة = واستخدمنا القوس {
        withContext(Dispatchers.IO) {
            try {
                supabaseClient.from("books").update(
                    mapOf("pagesread" to newPagesRead)
                ) {
                    filter {
                        eq("isbn", isbn)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("MAKTABA_API", "Error updating pages: ${e.message}")
            }
        }
    }

    override suspend fun uploadImage(fileName: String, byteArray: ByteArray): String? = withContext(Dispatchers.IO) {
        try {
            android.util.Log.d("MAKTABA_UPLOAD", "Starting upload: $fileName, size: ${byteArray.size}")

            // استخدمنا supabaseClient.storage
            val bucket = supabaseClient.storage.from("books_images")

            bucket.upload(path = fileName, data = byteArray, upsert = true)

            val url = bucket.publicUrl(fileName)

            android.util.Log.d("MAKTABA_UPLOAD", "Upload Success! Public URL: $url")
            url
        } catch (e: Exception) {
            android.util.Log.e("MAKTABA_STORAGE", "Detailed Upload Error: ${e.message}")
            null
        }
    }
}