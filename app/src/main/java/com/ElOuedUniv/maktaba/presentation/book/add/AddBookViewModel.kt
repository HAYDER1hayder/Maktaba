package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase // ✅ نحتاج هذا الجلب
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase, // ✅ لإحضار بيانات الكتاب المراد تعديله
    savedStateHandle: SavedStateHandle // ✅ لاستقبال الـ ISBN من شاشة التفاصيل
) : ViewModel() {

    // التقاط الـ ISBN (إذا كان null فهذا يعني عملية إضافة كتاب جديد)
    private val editBookIsbn: String? = savedStateHandle["isbn"]

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // ✅ إذا دخلنا في وضع التعديل، قم بتعبئة الحقول فوراً
        editBookIsbn?.let { isbn ->
            loadBookData(isbn)
        }
    }

    private fun loadBookData(isbn: String) {
        val book = getBookByIsbnUseCase(isbn)
        book?.let {
            _uiState.update { state ->
                state.copy(
                    isbn = it.isbn,
                    title = it.title,
                    nbPages = it.nbPages.toString(),
                    imageUrl = it.imageUrl,
                    isEditMode = true // نحتاج إضافة هذه الخاصية في الـ UiState
                )
            }
        }
    }

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
            is AddBookUiAction.OnImageSelected -> {
                _uiState.update { it.copy(imageUrl = action.uri) }
            }
            AddBookUiAction.OnRemoveImage -> {
                _uiState.update { it.copy(imageUrl = null) }
            }
            AddBookUiAction.OnAddClick -> {
                saveBook() // تغيير الاسم من addBook إلى saveBook ليكون أشمل
            }
        }
    }

    private fun saveBook() {
        val currentState = _uiState.value
        val book = Book(
            isbn = currentState.isbn,
            title = currentState.title,
            nbPages = currentState.nbPages.toIntOrNull() ?: 0,
            imageUrl = currentState.imageUrl
        )

        // هنا يتم الحفظ (في حالة التعديل، الـ Database ستقوم بعمل Update لأن الـ ISBN نفسه موجود)
        addBookUseCase(book)
        _uiState.update { it.copy(isSuccess = true) }
    }
}