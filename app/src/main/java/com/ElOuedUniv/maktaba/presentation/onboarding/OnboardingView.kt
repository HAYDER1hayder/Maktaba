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
import androidx.compose.ui.draw.drawWithContent
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
import kotlin.random.Random

data class LightTrace(val position: Offset, val isPermanent: Boolean = false)

@Composable
fun OnboardingView(
    onNavigateToLibrary: () -> Unit
) {
    // مصفوفة النقاط: الثابتة (الاسم) والمتحركة (رأس الليزر)
    val permanentTraces = remember { mutableStateListOf<Offset>() }
    val laserHead = remember { mutableStateOf(Offset(-500f, 0f)) }
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch {
            // خارطة طريق Haidar + النزول للزر
            val haidarPath = listOf(
                // H
                Offset(-180f, -40f), Offset(-180f, 40f), Offset(-180f, 0f), Offset(-100f, 0f), Offset(-100f, -40f), Offset(-100f, 40f),
                // a
                Offset(-60f, 40f), Offset(-20f, 40f), Offset(-20f, 0f), Offset(-60f, 0f), Offset(-60f, 40f),
                // i
                Offset(10f, 40f), Offset(10f, -10f),
                // d
                Offset(90f, -40f), Offset(90f, 40f), Offset(90f, 10f), Offset(50f, 10f), Offset(50f, 40f), Offset(90f, 40f),
                // a
                Offset(120f, 40f), Offset(160f, 40f), Offset(160f, 10f), Offset(120f, 10f), Offset(120f, 40f),
                // r
                Offset(190f, 40f), Offset(190f, 10f), Offset(220f, 10f),
                // النزول للزر (نقطة الشحن)
                Offset(20f, 350f)
            )

            for (target in haidarPath) {
                val startPos = laserHead.value
                val steps = 12
                for (s in 1..steps) {
                    val progress = s.toFloat() / steps
                    val nextX = startPos.x + (target.x - startPos.x) * progress
                    val nextY = startPos.y + (target.y - startPos.y) * progress

                    val newPoint = Offset(nextX, nextY)
                    laserHead.value = newPoint
                    // إضافة النقاط للمصفوفة الدائمة ليبقى الاسم مرسوماً
                    permanentTraces.add(newPoint)
                    delay(12)
                }
            }
            isReady = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(GeminiDeepSpace),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.height(300.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    translate(left = w / 2, top = h / 2) {
                        // ✅ رسم الاسم الذي "يبقى" بعد الليزر (الخيط الدائم)
                        if (permanentTraces.size > 1) {
                            val strokePath = Path()
                            strokePath.moveTo(permanentTraces[0].x, permanentTraces[0].y)
                            for (i in 1 until permanentTraces.size) {
                                strokePath.lineTo(permanentTraces[i].x, permanentTraces[i].y)
                            }
                            drawPath(
                                path = strokePath,
                                color = GeminiPurpleNeon,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
                                alpha = 0.8f // توهج ثابت للاسم
                            )
                        }

                        // ✅ رأس الليزر المشع (يختفي في النهاية)
                        if (!isReady) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color.White, GeminiPurpleNeon, Color.Transparent),
                                    center = laserHead.value,
                                    radius = 20.dp.toPx()
                                ),
                                radius = 20.dp.toPx(),
                                center = laserHead.value
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // ✅ الزر الشفاف تماماً بأسلوب نيون (Neon Glass)
            OutlinedButton(
                onClick = onNavigateToLibrary,
                enabled = isReady,
                modifier = Modifier
                    .height(60.dp)
                    .width(260.dp),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = if (isReady) GeminiPurpleNeon else Color.White.copy(0.1f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent, // شفاف تماماً كما طلبت
                    contentColor = if (isReady) Color.White else Color.White.copy(0.2f)
                )
            ) {
                Text(
                    text = if (isReady) "GET STARTED" else "WAITING...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}