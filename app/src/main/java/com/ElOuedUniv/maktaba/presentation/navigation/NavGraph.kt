package com.ElOuedUniv.maktaba.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ElOuedUniv.maktaba.presentation.book.BookListView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookViewModel
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailView
import com.ElOuedUniv.maktaba.presentation.category.CategoryListView
import com.ElOuedUniv.maktaba.presentation.onboarding.OnboardingView
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace // استيراد اللون الملكي

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        // إضافة خلفية سوداء عميقة للـ NavHost لمنع أي وميض أبيض أثناء التنقل
        modifier = Modifier.background(GeminiDeepSpace)
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingView(
                onNavigateToLibrary = {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BookList.route) {
            // استدعاء الواجهة الفخمة مع الربط الصحيح للأحداث
            BookListView(
                onCategoriesClick = { navController.navigate(Screen.CategoryList.route) },
                onAddBookClick = { navController.navigate(Screen.AddBook.route) },
                onBookClick = { isbn ->
                    navController.navigate(Screen.BookDetail.createRoute(isbn))
                }
            )
        }

        composable(Screen.BookDetail.route) {
            BookDetailView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.CategoryList.route) {
            CategoryListView(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.AddBook.route) {
            // استدعاء الـ ViewModel تلقائياً باستخدام Hilt
            val viewModel: AddBookViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            AddBookView(
                state = state,
                onAction = { action -> viewModel.onAction(action) },
                onBack = { navController.popBackStack() } // تأكد أن الاسم هنا onBack ليتوافق مع الـ View
            )
        }
    }
}