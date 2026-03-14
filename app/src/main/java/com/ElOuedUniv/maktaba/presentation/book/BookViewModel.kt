package com.ElOuedUniv.maktaba.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.GetBooksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
) : ViewModel() {

//    private val _books = MutableStateFlow<List<Book>>(emptyList())
//    val books: StateFlow<List<Book>> = _books.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getBooksUseCase()
                .catch { exception ->
                    // في حال حدوث خطأ، نقوم بتحديث رسالة الخطأ وإيقاف التحميل
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "حدث خطأ غير متوقع"
                        )
                    }
                }
                .collect { bookList ->
                    _uiState.update {
                        it.copy(
                            books = bookList,
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * TODO: Exercise 3 - Handle UI Actions
     */
    fun onAction(action: BookUiAction) {
        when (action) {
            BookUiAction.RefreshBooks -> refreshBooks()
            BookUiAction.OnAddBookClick -> {
                _uiState.update { it.copy(isAddingBook = true) }
            }
            BookUiAction.OnDismissAddBook -> {
                _uiState.update { it.copy(isAddingBook = false) }
            }
            is BookUiAction.OnAddBookConfirm -> {
                viewModelScope.launch {

                    val bookToSave = Book(
                        title = action.title,
                        isbn = action.isbn,
                        nbPages = action.nbPages
                    )
                    addBookUseCase(bookToSave)
                    _uiState.update { it.copy(isAddingBook = false) }
                }
            }
        }
    }

    fun refreshBooks() {
        loadBooks()
    }
}

