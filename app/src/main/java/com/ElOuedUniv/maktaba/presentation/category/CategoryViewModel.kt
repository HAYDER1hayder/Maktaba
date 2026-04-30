package com.ElOuedUniv.maktaba.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    // 1. إدارة الحالة (State) باستخدام الكلاس الذي أنشأته أنت
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    // 2. قناة للأحداث التي تحدث لمرة واحدة (مثل التنقل أو الخطأ)
    private val _uiEvent = MutableSharedFlow<CategoryUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // للحفاظ على التوافق مع الكود القديم في الـ View (إذا لم ترد تغيير الـ View حالياً)
    val categories = _uiState.map { it.categories }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isLoading = _uiState.map { it.isLoading }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        handleAction(CategoryUiAction.RefreshCategories)
    }

    /**
     * الدالة المركزية لمعالجة أي فعل يقوم به المستخدم
     */
    fun handleAction(action: CategoryUiAction) {
        when (action) {
            is CategoryUiAction.RefreshCategories -> loadCategories()
            is CategoryUiAction.OnBackClick -> {
                viewModelScope.launch { _uiEvent.emit(CategoryUiEvent.NavigateBack) }
            }
            is CategoryUiAction.OnCategoryClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(CategoryUiEvent.NavigateToBookList(action.categoryId))
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getCategoriesUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = exception.message)
                    }
                    _uiEvent.emit(CategoryUiEvent.ShowSnackbar(exception.message ?: "Unknown Error"))
                }
                .collect { categoryList ->
                    _uiState.update {
                        it.copy(categories = categoryList, isLoading = false)
                    }
                }
        }
    }
}