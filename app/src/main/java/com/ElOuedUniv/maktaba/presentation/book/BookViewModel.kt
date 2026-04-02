package com.ElOuedUniv.maktaba.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
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
    private val addBookUseCase: AddBookUseCase
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
                            // تحديث العداد الحقيقي كما يظهر في التصميم المستهدف
                            totalBooksCount = bookList.size
                        )
                    }
                }
        }
    }

    /**
     * معالجة الأفعال (UI Actions) لربط التصميم بالمنطق
     */
    fun onAction(action: BookUiAction) {
        when (action) {
            BookUiAction.RefreshBooks -> loadBooks()
            BookUiAction.OnAddBookClick -> {
                _uiState.update { it.copy(isAddingBook = true) }
            }
            BookUiAction.OnDismissAddBook -> {
                _uiState.update { it.copy(isAddingBook = false) }
            }
            is BookUiAction.OnAddBookConfirm -> {
                viewModelScope.launch {
                    val newBook = Book(
                        isbn = action.isbn,
                        title = action.title,
                        nbPages = action.nbPages
                    )
                    addBookUseCase(newBook)
                    _uiState.update { it.copy(isAddingBook = false) }
                    // لا نحتاج لاستدعاء loadBooks يدوياً لأن Flow سيحدث البيانات تلقائياً
                }
            }
            // تم إضافة التعامل مع البحث والملاحة لضمان عدم وجود أخطاء في الواجهة الفخمة
            is BookUiAction.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = action.query) }
            }
            is BookUiAction.OnBookClick -> {
                // الملاحة تتم عادة في الواجهة، ولكن الأكشن هنا لضمان تتبع الحدث
            }
        }
    }
}