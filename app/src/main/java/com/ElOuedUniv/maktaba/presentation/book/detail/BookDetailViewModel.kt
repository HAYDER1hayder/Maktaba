package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase
) : ViewModel() {

    private val isbn: String = checkNotNull(savedStateHandle["isbn"])
    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BookDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init { loadBook() }

    fun loadBook() {
        _uiState.update { it.copy(isLoading = true) }

        // جلب الكتاب الأصلي من قاعدة البيانات
        val originalBook = getBookByIsbnUseCase(isbn)

        // ✅ حقن "التعديلات المجنونة": محاكاة عدد صفحات مقروءة عشوائي
        // في المستقبل، سيأتي هذا الرقم مباشرة من قاعدة البيانات
        val mockPagesRead = if (originalBook != null) {
            Random.nextInt(0, originalBook.nbPages + 1)
        } else 0

        val updatedBook = originalBook?.copy(
            pagesRead = mockPagesRead
        )

        _uiState.update { it.copy(isLoading = false, book = updatedBook) }
    }

    fun onAction(action: BookDetailUiAction) {
        viewModelScope.launch {
            when (action) {
                is BookDetailUiAction.OnBackClick -> _uiEvent.emit(BookDetailUiEvent.NavigateBack)
                is BookDetailUiAction.OnEditClick -> _uiEvent.emit(BookDetailUiEvent.NavigateToEdit(isbn))
                is BookDetailUiAction.OnCoverClick -> _uiEvent.emit(BookDetailUiEvent.LaunchGallery)
                is BookDetailUiAction.OnDeleteClick -> { /* Logic */ }
                is BookDetailUiAction.OnToggleStatus -> { /* Logic */ }
            }
        }
    }
}