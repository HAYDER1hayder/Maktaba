package com.ElOuedUniv.maktaba.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- تعريف مصفوفة الألوان الفاخرة ---
private val DarkColorScheme = darkColorScheme(
    primary = GeminiPurpleNeon,      // البنفسجي المشع كأصل للنظام
    secondary = GeminiPinkNeon,       // الوردي للمسات الإضافية
    tertiary = GeminiGlassCard,      // لون الكروت الزجاجية
    background = GeminiDeepSpace,    // الخلفية السوداء العميقة
    surface = GeminiGlassCard,       // الأسطح الزجاجية
    onPrimary = GeminiTextPrimary,
    onBackground = GeminiTextPrimary,
    onSurface = GeminiTextPrimary
)

@Composable
fun MaktabaTheme(
    // سنجعل الوضع المظلم هو الافتراضي دائماً للتصميم الفخم
    darkTheme: Boolean = true,
    // نعطل الألوان الديناميكية لضمان ثبات ألوان Gemini VIP بنسبة 100%
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // نستخدم ألواننا الخاصة دائماً

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // جعل شريط الحالة (StatusBar) أسود ليتماشى مع الفخامة
            window.statusBarColor = GeminiDeepSpace.toArgb()
            window.navigationBarColor = GeminiDeepSpace.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}