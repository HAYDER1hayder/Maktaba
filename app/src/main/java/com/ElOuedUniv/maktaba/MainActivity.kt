package com.ElOuedUniv.maktaba

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.ElOuedUniv.maktaba.presentation.MainViewModel
import com.ElOuedUniv.maktaba.presentation.navigation.NavGraph
import com.ElOuedUniv.maktaba.presentation.navigation.Screen
import com.ElOuedUniv.maktaba.presentation.theme.MaktabaTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    // 👇 1. نحقن عميل Supabase هنا لكي نعطيه رسالة الدخول
    @Inject
    lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 👇 2. إذا تم فتح التطبيق من المتصفح لأول مرة، نسلم الرابط لـ Supabase
        supabaseClient.handleDeeplinks(intent)

        setContent {
            MaktabaTheme {
                val startDestination = mainViewModel.startDestination
                val navController = rememberNavController()

                if (startDestination != null) {
                    LaunchedEffect(startDestination) {
                        if (startDestination == Screen.BookList.route) {
                            navController.navigate(Screen.BookList.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    // 👇 3. هذا هو السحر الأهم: إذا كان التطبيق مفتوحاً في الخلفية وعاد المتصفح إليه
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // نسلم الرابط لـ Supabase ليقوم بتسجيل الدخول فوراً
        supabaseClient.handleDeeplinks(intent)
    }
}