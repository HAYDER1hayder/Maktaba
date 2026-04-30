package com.ElOuedUniv.maktaba.presentation.book.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase,
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editBookIsbn: String? = savedStateHandle["isbn"]
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    private var pendingImageBytes: ByteArray? = null

    init {
        editBookIsbn?.let { isbn ->
            viewModelScope.launch { loadBookData(isbn) }
        }
    }

    private suspend fun loadBookData(isbn: String) {
        val book = getBookByIsbnUseCase(isbn)
        book?.let {
            _uiState.update { state ->
                state.copy(
                    isbn = it.isbn,
                    title = it.title,
                    nbPages = it.nbPages.toString(),
                    pagesRead = it.pagesRead.toString(),
                    imageUrl = it.imageUrl,
                    isEditMode = true
                )
            }
            validateForm()
        }
    }

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                if (action.title.length <= 100) {
                    _uiState.update { it.copy(title = action.title) }
                    validateForm()
                }
            }
            is AddBookUiAction.OnIsbnChange -> handleIsbnInput(action.isbn)
            is AddBookUiAction.OnPagesChange -> {
                val digits = action.pages.filter { it.isDigit() }
                if (digits.length <= 5) {
                    _uiState.update { it.copy(nbPages = digits) }
                    validateForm()
                }
            }
            // داخل ViewModel في دالة onAction
            is AddBookUiAction.OnPagesReadChange -> {
                val input = action.pagesRead.filter { it.isDigit() }

                // جلب إجمالي الصفحات من الحالة الحالية
                val total = _uiState.value.nbPages.toIntOrNull() ?: 0
                val currentInput = input.toIntOrNull() ?: 0

                // الحل: السماح بالنص الفارغ لتمكين المسح، والسماح بالصفر
                if (input.isEmpty()) {
                    _uiState.update { it.copy(pagesRead = "") } // سيظهر الحقل فارغاً ولكننا سنعامله كـ 0 عند الحفظ
                } else if (total == 0 || currentInput <= total) {
                    // نحدث الحالة بالقيمة المدخلة كما هي (حتى لو كانت "0")
                    _uiState.update { it.copy(pagesRead = input) }
                }
                validateForm()
            }
            is AddBookUiAction.OnImageSelected -> {
                pendingImageBytes = action.imageBytes
                _uiState.update { it.copy(imageUrl = action.uri) }
                validateForm()
            }
            AddBookUiAction.OnRemoveImage -> {
                _uiState.update { it.copy(imageUrl = null) }
                pendingImageBytes = null
                validateForm()
            }
            AddBookUiAction.OnAddClick -> {
                if (_uiState.value.isButtonEnabled) {
                    viewModelScope.launch { saveBook() }
                }
            }
        }
    }

    private fun handleIsbnInput(input: String) {
        val digitsOnly = input.filter { it.isDigit() }
        if (digitsOnly.length <= 13) {
            val formatted = if (digitsOnly.length > 3) {
                "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3)}"
            } else {
                digitsOnly
            }
            _uiState.update { it.copy(isbn = formatted) }
            validateForm()
        }
    }

    private fun validateForm() {
        val state = _uiState.value
        val totalPages = state.nbPages.toIntOrNull() ?: 0
        val readPages = state.pagesRead.toIntOrNull() ?: 0

        // السماح بـ 0 بشكل صريح وضمان عدم تجاوز الإجمالي
        val isValid = state.title.isNotBlank() &&
                state.isbn.length >= 4 &&
                state.nbPages.isNotBlank() &&
                state.pagesRead.isNotBlank() &&
                readPages >= 0 && 
                (state.isEditMode.not() || readPages <= totalPages)

        _uiState.update { it.copy(isButtonEnabled = isValid) }
    }

    private suspend fun saveBook() {
        _uiState.update { it.copy(isLoading = true) }
        val currentState = _uiState.value
        var finalImageUrl = if (currentState.imageUrl?.startsWith("http") == true) currentState.imageUrl else null

        pendingImageBytes?.let { bytes ->
            val fileName = "public/book_${System.currentTimeMillis()}.jpg"
            val uploadedUrl = bookRepository.uploadImage(fileName, bytes)
            if (uploadedUrl != null) {
                finalImageUrl = uploadedUrl
            }
        }

        val book = Book(
            isbn = currentState.isbn,
            title = currentState.title,
            nbPages = currentState.nbPages.toIntOrNull() ?: 0,
            pagesRead = currentState.pagesRead.toIntOrNull() ?: 0,
            imageUrl = finalImageUrl
        )
        
        try {
            addBookUseCase(book)
            _uiState.update { it.copy(isSuccess = true, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
