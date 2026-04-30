package com.ElOuedUniv.maktaba.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingView(
    onNavigateToLibrary: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    // 🎨 استخراج الألوان من صور تطبيقك الداخلية للحفاظ على الهوية
    val appBackground = Color(0xFF090A0F) // أسود عميق جداً
    val cardBackground = Color(0xFF11141A) // لون البطاقات الداخلي
    val neonGreen = Color(0xFF00E676) // لون النيون الأخضر (SECURED)
    val neonPurple = Color(0xFFAB47BC) // لون النيون البنفسجي (زر +)
    val textWhite = Color(0xFFF0F0F0)
    val textGray = Color(0xFF8B949E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground)
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(0.3f))

        // 1. الدائرة العلوية (اللوجو البرمجي الفخم)
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1E232D), appBackground)
                    )
                )
                .border(1.5.dp, neonGreen.copy(alpha = 0.4f), CircleShape), // إطار أخضر خفيف
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = neonGreen // أيقونة بلون النيون الأخضر
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // 2. المربع في المنتصف (حسب رسمتك)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackground, shape = RoundedCornerShape(20.dp))
                .border(1.5.dp, neonGreen.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .padding(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "INITIALIZE ENTRY", // عنوان بطابع تقني يناسب تطبيقك
                    color = neonGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sync your reading archive securely.\nAuthenticate via GitHub to access encrypted library features.",
                    color = textGray,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 3. الأزرار بشكل متدرج (حسب رسمتك التخطيطية تماماً!)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // الزر الأول (GitHub) - مزاح لليسار
            Button(
                onClick = { viewModel.onLoginWithGithubClick() },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // يأخذ 80% من العرض
                    .height(56.dp)
                    .align(Alignment.Start), // محاذاة لليسار
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = neonPurple, // لون بنفسجي ليتناسق مع تطبيقك
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "GitHub Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // الزر الثاني (كضيف) - مزاح لليمين
            OutlinedButton(
                onClick = { onNavigateToLibrary() },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // يأخذ 80% من العرض
                    .height(56.dp)
                    .align(Alignment.End), // محاذاة لليمين
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = neonGreen
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, neonGreen) // إطار أخضر
            ) {
                Text(
                    text = "Guest Access",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}