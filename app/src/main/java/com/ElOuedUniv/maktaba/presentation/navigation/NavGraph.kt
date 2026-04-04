package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType // تأكد من وجود هذا
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument // تأكد من وجود هذا
import com.ElOuedUniv.maktaba.presentation.book.BookListView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookViewModel
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailView
import com.ElOuedUniv.maktaba.presentation.category.CategoryListView
import com.ElOuedUniv.maktaba.presentation.onboarding.OnboardingView
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        modifier = Modifier.background(GeminiDeepSpace)
    ) {
        // 1. شاشة الترحيب
        composable(Screen.Onboarding.route) {
            OnboardingView(
                onNavigateToLibrary = {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. شاشة قائمة الكتب
        composable(Screen.BookList.route) {
            BookListView(
                onCategoriesClick = { navController.navigate(Screen.CategoryList.route) },
                onAddBookClick = { navController.navigate(Screen.AddBook.route) },
                onBookClick = { isbn ->
                    navController.navigate(Screen.BookDetail.createRoute(isbn))
                }
            )
        }

        // 3. شاشة تفاصيل الكتاب
        composable(Screen.BookDetail.route) {
            BookDetailView(
                onBackClick = { navController.popBackStack() },
                onNavigateToEdit = { isbn ->
                    // نرسل الـ isbn كـ Query Parameter باستخدام علامة الاستفهام
                    navController.navigate(Screen.AddBook.route + "?isbn=$isbn")
                }
            )
        }

        // 4. شاشة قائمة التصنيفات
        composable(Screen.CategoryList.route) {
            CategoryListView(onBackClick = { navController.popBackStack() })
        }

        // 5. شاشة إضافة / تعديل الكتاب (هنا التعديل الجوهري)
        composable(
            route = Screen.AddBook.route + "?isbn={isbn}",
            arguments = listOf(
                navArgument("isbn") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val viewModel: AddBookViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            AddBookView(
                state = state,
                onAction = { action -> viewModel.onAction(action) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
