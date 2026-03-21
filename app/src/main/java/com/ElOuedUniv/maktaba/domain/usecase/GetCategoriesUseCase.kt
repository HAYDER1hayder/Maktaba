package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.data.repository.CategoryRepository

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    /**
     * جلب جميع التصنيفات وترتيبها أبجدياً حسب الاسم (Bonus 3)
     */
    operator fun invoke(): List<Category> {
        return categoryRepository.getAllCategories().sortedBy { it.name }
    }
}