package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // تم حقن الـ UseCase بنجاح هنا بواسطة Hilt
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase
) : ViewModel() {

    private val isbn: String = checkNotNull(savedStateHandle["isbn"])

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BookDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // ❌ تم حذف السطر المسبب للمشكلة: private val bookRepository = BookRepositoryImpl()
    // نحن لا ننشئ كلاسات يدوياً، ولا نتحدث مع الـ Repository مباشرة من الـ ViewModel

    init {
        viewModelScope.launch {
            loadBook()
        }
    }

    fun updateProgress(additionalPages: String) {
        val additional = additionalPages.toIntOrNull() ?: 0
        val currentBook = uiState.value.book ?: return

        val newTotal = (currentBook.pagesRead + additional).coerceAtMost(currentBook.nbPages)

        viewModelScope.launch {
            // ✅ تم استبدال bookRepository بالـ UseCase الجاهز والمحقون
            val book = getBookByIsbnUseCase(isbn)

            // ملاحظة: هنا ستحتاج لاحقاً لاستدعاء UseCase جديد لتحديث الصفحات المقروءة في قاعدة البيانات
            // مثلاً: updateBookProgressUseCase(isbn, newTotal)
        }
    }

    suspend fun loadBook() {
        _uiState.update { it.copy(isLoading = true) }

        // هنا أنت كنت تستخدم الـ UseCase بشكل صحيح وممتاز!
        val originalBook = getBookByIsbnUseCase(isbn)

        _uiState.update {
            it.copy(
                isLoading = false,
                book = originalBook
            )
        }
    }

    fun onAction(action: BookDetailUiAction) {
        viewModelScope.launch {
            when (action) {
                is BookDetailUiAction.OnBackClick -> _uiEvent.emit(BookDetailUiEvent.NavigateBack)
                is BookDetailUiAction.OnEditClick -> _uiEvent.emit(BookDetailUiEvent.NavigateToEdit(isbn))
                is BookDetailUiAction.OnCoverClick -> _uiEvent.emit(BookDetailUiEvent.LaunchGallery)
                is BookDetailUiAction.OnDeleteClick -> {}
                is BookDetailUiAction.OnToggleStatus -> {}
            }
        }
    }
}