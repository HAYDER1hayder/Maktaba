package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase
) : ViewModel() {

    private val isbn: String = checkNotNull(savedStateHandle["isbn"])

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState = _uiState.asStateFlow()

    // ✅ تم تحديد النوع <BookDetailUiEvent> صراحةً لحل الخطأ
    private val _uiEvent = MutableSharedFlow<BookDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        _uiState.update { it.copy(isLoading = true) }
        val book = getBookByIsbnUseCase(isbn)
        _uiState.update { it.copy(isLoading = false, book = book) }
    }

    fun onAction(action: BookDetailUiAction) {
        when (action) {
            // في ملف BookDetailViewModel.kt
            is BookDetailUiAction.OnBackClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(BookDetailUiEvent.NavigateBack)
                }
            }

            is BookDetailUiAction.OnEditClick -> {
                viewModelScope.launch {
                    // تأكد من تمرير الـ isbn المعرف في أعلى الـ ViewModel
                    _uiEvent.emit(BookDetailUiEvent.NavigateToEdit(isbn))
                }
            }
            is BookDetailUiAction.OnDeleteClick -> { /* منطق الحذف */ }
            is BookDetailUiAction.OnToggleStatus -> { /* منطق الحالة */ }
        }
    }
}