package com.ElOuedUniv.maktaba.presentation.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingView(
    onNavigateToLibrary: () -> Unit
) {
    // مصفوفة النقاط التي تشكل الاسم (الخيط فقط)
    val permanentPoints = remember { mutableStateListOf<Offset>() }
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch {
            // ✅ المسار المقسم لضمان الانسيابية (H-ai-d-ar)
            val fullPath = listOf(
                // H
                Offset(-280f, -100f), Offset(-280f, 100f), Offset(-280f, 0f), Offset(-180f, 0f), Offset(-180f, -100f), Offset(-180f, 100f),
                // ai
                Offset(-130f, 100f), Offset(-60f, 100f), Offset(-30f, 50f), Offset(-60f, 0f), Offset(-130f, 50f), Offset(-130f, 100f), Offset(-20f, 100f), Offset(-20f, 0f),
                // d
                Offset(100f, -100f), Offset(100f, 100f), Offset(100f, 40f), Offset(30f, 70f), Offset(30f, 100f), Offset(100f, 100f),
                // ar
                Offset(140f, 100f), Offset(200f, 50f), Offset(140f, 50f), Offset(200f, 100f), Offset(240f, 100f), Offset(240f, 40f), Offset(300f, 40f),
                // النزول للزر
                Offset(50f, 480f)
            )

            for (i in 0 until fullPath.size - 1) {
                val start = fullPath[i]
                val end = fullPath[i+1]
                val steps = 20

                for (s in 1..steps) {
                    val t = s.toFloat() / steps
                    val nextPos = Offset(
                        start.x + (end.x - start.x) * t,
                        start.y + (end.y - start.y) * t
                    )
                    permanentPoints.add(nextPos)
                    delay(10) // سرعة الرسم
                }
            }
            isReady = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(GeminiDeepSpace),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.height(400.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    translate(left = w / 2, top = h / 2) {
                        // ✅ رسم أثر الخيط فقط (بدون أي ضوء في الرأس)
                        if (permanentPoints.size > 3) {
                            val strokePath = Path()
                            strokePath.moveTo(permanentPoints[0].x, permanentPoints[0].y)

                            // تنعيم المنحنيات لإلغاء الزوايا الحادة
                            for (i in 1 until permanentPoints.size - 2) {
                                val p1 = permanentPoints[i]
                                val p2 = permanentPoints[i+1]
                                val midX = (p1.x + p2.x) / 2
                                val midY = (p1.y + p2.y) / 2
                                strokePath.quadraticBezierTo(p1.x, p1.y, midX, midY)
                            }

                            drawPath(
                                path = strokePath,
                                color = GeminiPurpleNeon,
                                style = Stroke(
                                    width = 5.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ✅ الزر الشفاف النيوني (Glass Design)
            OutlinedButton(
                onClick = onNavigateToLibrary,
                enabled = isReady,
                modifier = Modifier.height(65.dp).width(280.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = if (isReady) GeminiPurpleNeon else Color.White.copy(0.1f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (isReady) Color.White else Color.White.copy(0.2f)
                )
            ) {
                Text(
                    text = if (isReady) "GET STARTED" else "CHARGING...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}