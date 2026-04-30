package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    
    fun getAllBooks(): Flow<List<Book>>

    suspend fun getBookByIsbn(isbn: String): Book?

    // في BookRepository.kt
    suspend fun updatePagesRead(isbn: String, newPagesRead: Int)


    suspend fun addBook(book: Book)
    suspend fun uploadImage(fileName: String, byteArray: ByteArray): String?
}
