package com.ElOuedUniv.maktaba.presentation.category

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.presentation.category.CategoryViewModel
// استيراد ألوان النيون الخاصة بمشروعك
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace
import com.ElOuedUniv.maktaba.presentation.theme.GeminiGlassCard
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon
import com.ElOuedUniv.maktaba.presentation.theme.GeminiTextSecondary

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        listOf(GeminiPurpleNeon.copy(alpha = 0.05f), GeminiDeepSpace)
                    )
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GeminiPurpleNeon
                )
            } else {
                if (categories.isEmpty()) {
                    EmptyCategoriesMessage(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    // استبدال القائمة العادية بقائمة الـ VIP
                    CategoryList(
                        categories = categories,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(20.dp), spotColor = GeminiPurpleNeon)
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(20.dp),
        color = GeminiGlassCard,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // أيقونة VIP دائرية متوهجة
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(GeminiPurpleNeon.copy(alpha = 0.1f), CircleShape)
                    .border(1.dp, GeminiPurpleNeon.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category.name),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = GeminiPurpleNeon
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )

                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = GeminiTextSecondary,
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

// دالة لربط الأسماء بالأيقونات الفخمة
fun getCategoryIcon(name: String): ImageVector {
    return when (name) {
        "Programming" -> Icons.Default.Terminal
        "Algorithms" -> Icons.Default.Hub
        "Databases" -> Icons.Default.Storage
        else -> Icons.Default.AutoAwesome
    }
}

@Composable
fun EmptyCategoriesMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.FolderOpen,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = GeminiPurpleNeon.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No categories available",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}