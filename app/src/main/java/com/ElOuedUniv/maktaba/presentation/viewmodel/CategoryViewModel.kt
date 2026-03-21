package com.ElOuedUniv.maktaba.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // تم تفعيل الـ UseCase بشكل نهائي وحذف الـ Dummy data
                val categoryList = getCategoriesUseCase()
                _categories.value = categoryList
            } catch (e: Exception) {
                // يمكن إضافة معالجة للأخطاء هنا إذا لزم الأمر
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Bonus 2: البحث عن تصنيف بواسطة المعرف (ID) ---
    fun getCategoryById(id: String): Category? {
        return categories.value.find { it.id == id }
    }

    fun refreshCategories() {
        loadCategories()
    }
}