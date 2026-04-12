package com.ElOuedUniv.maktaba.presentation.category

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListView(
    onBackClick: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        containerColor = GeminiDeepSpace,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "DISCOVER CATEGORIES",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GeminiPurpleNeon
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // مسافة بسيطة بين الكروت
                ) {
                    itemsIndexed(categories) { index, category ->
                        CategoryItem(category = category, index = index)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, index: Int) {
    val cardAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(Unit) {
        delay(index * 130L)
        cardAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
        )
    }

    // تبادل الألوان كما في الواجهة الرئيسية
    val themeColor = if (index % 2 == 0) Color(0xFF00FFD1) else GeminiPurpleNeon

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp) // ارتفاع ضخم للهيبة
            .graphicsLayer {
                translationY = (100 * (1f - cardAnimation.value)).dp.toPx()
                alpha = cardAnimation.value
                scaleX = 0.95f + (0.05f * cardAnimation.value)
                scaleY = 0.95f + (0.05f * cardAnimation.value)
            }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // ✅ الطبقة الخلفية (بدون إزاحة يميناً أو يساراً لضمان خط واحد)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 8.dp) // إزاحة لأسفل فقط لعمق الـ 3D
                .background(themeColor.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                .border(1.dp, themeColor.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
        )

        // ✅ الكرت الأساسي (الواقعي والمستقيم)
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = themeColor.copy(alpha = 0.4f)
                )
                .clickable { /* Handle click */ },
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121212), // نفس لون كروت الواجهة الرئيسية
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(themeColor.copy(alpha = 0.5f), Color.Transparent)
                )
            )
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                // الشارة العلوية
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.5f),
                    border = BorderStroke(0.5.dp, themeColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "SECURED DATA",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = themeColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // المحتوى السفلي
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Icon(
                        imageVector = getCategoryIcon(category.name),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = themeColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = category.name.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )

                    Text(
                        text = "ACCESS LEVEL: FULL",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.3f),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

fun getCategoryIcon(name: String): ImageVector {
    return when {
        name.contains("Program", true) -> Icons.Default.Terminal
        name.contains("Algo", true) -> Icons.Default.Hub
        name.contains("Data", true) -> Icons.Default.Storage
        name.contains("Security", true) -> Icons.Default.Shield
        else -> Icons.Default.AutoAwesome
    }
}