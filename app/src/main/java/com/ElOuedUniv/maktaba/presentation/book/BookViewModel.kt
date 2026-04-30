package com.ElOuedUniv.maktaba.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.domain.usecase.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getBooksUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { bookList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            books = bookList,
                            totalBooksCount = bookList.size
                        )
                    }
                }
        }
    }

    private var selectedImageByteArray: ByteArray? = null

    fun onImageSelected(byteArray: ByteArray?) {
        selectedImageByteArray = byteArray
    }

    fun onAction(action: BookUiAction) {
        when (action) {
            BookUiAction.RefreshBooks -> loadBooks()
            BookUiAction.OnAddBookClick -> {
                _uiState.update { it.copy(isAddingBook = true) }
            }
            BookUiAction.OnDismissAddBook -> {
                _uiState.update { it.copy(isAddingBook = false) }
                selectedImageByteArray = null
            }
            is BookUiAction.OnAddBookConfirm -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    
                    // منع حفظ روابط content:// المحلية
                    var finalImageUrl: String? = if (action.imageUrl?.startsWith("http") == true) action.imageUrl else null
                    
                    // رفع الصورة للسحاب إذا وجدت بيانات بايتات
                    selectedImageByteArray?.let { bytes ->
                        val fileName = "book_${System.currentTimeMillis()}.jpg"
                        val uploadedUrl = bookRepository.uploadImage(fileName, bytes)
                        if (uploadedUrl != null) {
                            finalImageUrl = uploadedUrl
                        }
                    }

                    val newBook = Book(
                        isbn = action.isbn,
                        title = action.title,
                        nbPages = action.nbPages,
                        imageUrl = finalImageUrl,
                        status = "Reading"
                    )
                    
                    try {
                        bookRepository.addBook(newBook)
                    } catch (e: Exception) {
                        android.util.Log.e("MAKTABA_UI", "Error: ${e.message}")
                    }
                    
                    _uiState.update { it.copy(isAddingBook = false, isLoading = false) }
                    selectedImageByteArray = null
                }
            }
            is BookUiAction.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = action.query) }
            }
            is BookUiAction.OnBookClick -> { }
        }
    }
}
