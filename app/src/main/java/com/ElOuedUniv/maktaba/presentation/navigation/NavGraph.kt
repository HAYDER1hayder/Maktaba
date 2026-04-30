package com.ElOuedUniv.maktaba.presentation.navigation

// ... (تأكد من إبقاء كل الـ imports الخاصة بك كما هي في ملفك الأصلي)
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ElOuedUniv.maktaba.presentation.book.BookListView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookView
import com.ElOuedUniv.maktaba.presentation.book.add.AddBookViewModel
import com.ElOuedUniv.maktaba.presentation.book.detail.BookDetailView
import com.ElOuedUniv.maktaba.presentation.category.CategoryListView
import com.ElOuedUniv.maktaba.presentation.onboarding.OnboardingView
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String // 👇 الإضافة هنا: نقطة البداية أصبحت ديناميكية
) {
    NavHost(
        navController = navController,
        startDestination = startDestination, // 👇 نستخدم المتغير هنا
        modifier = Modifier.background(GeminiDeepSpace)
    ) {
        // ... (باقي الكود والشاشات من 1 إلى 5 تظل كما هي تماماً بدون أي تغيير) ...

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
            BookListView(
                onCategoriesClick = { navController.navigate(Screen.CategoryList.route) },
                onAddBookClick = { navController.navigate(Screen.AddBook.route) },
                onBookClick = { isbn ->
                    navController.navigate(Screen.BookDetail.createRoute(isbn))
                }
            )
        }

        composable(Screen.BookDetail.route) {
            BookDetailView(
                onBackClick = { navController.popBackStack() },
                onNavigateToEdit = { isbn ->
                    navController.navigate(Screen.AddBook.route + "?isbn=$isbn")
                }
            )
        }

        composable(Screen.CategoryList.route) {
            CategoryListView(onBackClick = { navController.popBackStack() })
        }

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
            val scope = rememberCoroutineScope()

            AddBookView(
                state = state,
                onAction = { action ->
                    scope.launch { viewModel.onAction(action) }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}