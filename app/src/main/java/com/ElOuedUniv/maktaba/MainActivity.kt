package com.ElOuedUniv.maktaba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect // 👈 استيراد مهم
import androidx.navigation.compose.rememberNavController // 👈 استيراد مهم
import com.ElOuedUniv.maktaba.presentation.MainViewModel
import com.ElOuedUniv.maktaba.presentation.navigation.NavGraph
import com.ElOuedUniv.maktaba.presentation.navigation.Screen // 👈 تأكد من استيراد مسار Screen الصحيح
import com.ElOuedUniv.maktaba.presentation.theme.MaktabaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaktabaTheme {
                val startDestination = mainViewModel.startDestination
                val navController = rememberNavController() // ننشئ متحكم الملاحة هنا

                if (startDestination != null) {

                    // 👇 هذا هو السحر: يراقب الحالة، وبمجرد أن تتغير إلى "مسجل الدخول"، ينقلك للمكتبة فوراً
                    LaunchedEffect(startDestination) {
                        if (startDestination == Screen.BookList.route) {
                            navController.navigate(Screen.BookList.route) {
                                // نمسح شاشة الترحيب من الخلفية حتى لا يعود إليها عند ضغط زر الرجوع
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
}