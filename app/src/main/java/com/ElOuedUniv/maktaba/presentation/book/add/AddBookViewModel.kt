package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }
            is AddBookUiAction.OnIsbnChange -> {
                _uiState.update { it.copy(isbn = action.isbn) }
            }
            is AddBookUiAction.OnPagesChange -> {
                _uiState.update { it.copy(nbPages = action.pages) }
            }
            // الإضافة الجديدة 1: معالجة اختيار الصورة
            is AddBookUiAction.OnImageSelected -> {
                _uiState.update { it.copy(imageUrl = action.uri) }
            }
            // الإضافة الجديدة 2: معالجة مسح الصورة
            AddBookUiAction.OnRemoveImage -> {
                _uiState.update { it.copy(imageUrl = null) }
            }
            AddBookUiAction.OnAddClick -> {
                addBook()
            }
        }
    }

    private fun addBook() {
        val currentState = _uiState.value
        val book = Book(
            isbn = currentState.isbn,
            title = currentState.title,
            nbPages = currentState.nbPages.toIntOrNull() ?: 0,
            imageUrl = currentState.imageUrl // الإضافة الجديدة 3: تمرير الصورة للكائن Book
        )
        addBookUseCase(book)
        _uiState.update { it.copy(isSuccess = true) }
    }
}