package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import com.ElOuedUniv.maktaba.domain.usecase.GetBookByIsbnUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editBookIsbn: String? = savedStateHandle["isbn"]
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    init {
        editBookIsbn?.let { isbn -> loadBookData(isbn) }
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
                    isEditMode = true
                )
            }
            validateForm() // تحديث حالة الزر بعد جلب البيانات
        }
    }

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange -> {
                // 1. حد العنوان: 100 حرف فقط
                if (action.title.length <= 100) {
                    _uiState.update { it.copy(title = action.title) }
                    validateForm()
                }
            }
            is AddBookUiAction.OnIsbnChange -> {
                // 2. معالجة الـ ISBN (أرقام فقط + شحطة تلقائية)
                handleIsbnInput(action.isbn)
            }
            is AddBookUiAction.OnPagesChange -> {
                // 3. حد الصفحات: 10,000 صفحة (5 خانات)
                val digits = action.pages.filter { it.isDigit() }
                if (digits.length <= 5 && (digits.toIntOrNull() ?: 0) <= 10000) {
                    _uiState.update { it.copy(nbPages = digits) }
                    validateForm()
                }
            }
            is AddBookUiAction.OnImageSelected -> {
                _uiState.update { it.copy(imageUrl = action.uri) }
                validateForm()
            }
            AddBookUiAction.OnRemoveImage -> {
                _uiState.update { it.copy(imageUrl = null) }
                validateForm()
            }
            AddBookUiAction.OnAddClick -> {
                if (_uiState.value.isButtonEnabled) saveBook()
            }
        }
    }

    private fun handleIsbnInput(input: String) {
        // حذف أي شحطات قديمة لمعالجة الأرقام الصافية
        val digitsOnly = input.filter { it.isDigit() }

        if (digitsOnly.length <= 13) {
            val formatted = if (digitsOnly.length > 3) {
                // ✅ إضافة الشحطة تلقائياً بعد الرقم الثالث
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
        // ✅ الشرط: لا يتفعل الزر إلا إذا كانت الحقول الثلاثة ممتلئة
        val isValid = state.title.isNotBlank() &&
                state.isbn.length >= 4 && // التأكد من كتابة 3 أرقام + شحطة + رقم واحد على الأقل
                state.nbPages.isNotBlank()

        _uiState.update { it.copy(isButtonEnabled = isValid) }
    }

    private fun saveBook() {
        val currentState = _uiState.value
        val book = Book(
            isbn = currentState.isbn,
            title = currentState.title,
            nbPages = currentState.nbPages.toIntOrNull() ?: 0,
            imageUrl = currentState.imageUrl
        )
        addBookUseCase(book)
        _uiState.update { it.copy(isSuccess = true) }
    }
}