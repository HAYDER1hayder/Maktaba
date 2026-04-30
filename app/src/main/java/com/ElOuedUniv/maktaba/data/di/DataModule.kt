package com.ElOuedUniv.maktaba.data.di

import com.ElOuedUniv.maktaba.data.repository.AuthRepositoryImpl
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.data.repository.BookRepositoryImpl
import com.ElOuedUniv.maktaba.data.repository.CategoryRepository
import com.ElOuedUniv.maktaba.data.repository.CategoryRepositoryImpl
import com.ElOuedUniv.maktaba.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository {
        return categoryRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        bookRepositoryImpl: BookRepositoryImpl
    ): BookRepository {
        return bookRepositoryImpl
    }

    // 👇 التعديل هنا: لقد حذفنا كلمة abstract واستخدمنا provideAuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository {
        return authRepositoryImpl
    }
}