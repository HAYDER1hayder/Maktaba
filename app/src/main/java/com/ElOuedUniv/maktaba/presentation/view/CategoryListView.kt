package com.ElOuedUniv.maktaba.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ElOuedUniv.maktaba.data.model.Category
import com.ElOuedUniv.maktaba.presentation.viewmodel.CategoryViewModel

// --- توحيد باليت الألوان النيون مع شاشة الكتب ---
private val DeepDark = Color(0xFF0F0F12)
private val SurfaceDark = Color(0xFF1E1E24)
private val NeonPurple = Color(0xFF8B5CF6)
private val SoftPurple = Color(0xFFC084FC)
private val AccentPink = Color(0xFFEC4899)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListView(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        containerColor = DeepDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Categories",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(listOf(Color.White, SoftPurple))
                        )
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepDark
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DeepDark)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = NeonPurple
                )
            } else {
                if (categories.isEmpty()) {
                    EmptyCategoriesMessage(modifier = Modifier.align(Alignment.Center))
                } else {
                    CategoryListContent(categories = categories)
                }
            }
        }
    }
}

@Composable
fun CategoryListContent(categories: List<Category>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // الـ Header الملون بنفس أسلوب شاشة الكتب
        item {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(NeonPurple, AccentPink))),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Collections", color = Color.White.copy(0.8f), fontSize = 12.sp)
                    Text("${categories.size}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        items(categories) { category ->
            ModernCategoryItem(category = category)
        }
    }
}

@Composable
fun ModernCategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // أيقونة المجلد بخلفية داكنة ونبض أرجواني
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = DeepDark
            ) {
                Icon(
                    imageVector = Icons.Default.FolderCopy,
                    contentDescription = null,
                    tint = SoftPurple,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (category.description.isNotEmpty()) {
                    Text(
                        text = category.description,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // سهم صغير للدلالة على إمكانية الدخول
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // سهم معكوس للدلالة على "Go"
                contentDescription = null,
                tint = Color.White.copy(0.2f),
                modifier = Modifier.size(16.dp).clip(CircleShape)
            )
        }
    }
}

@Composable
fun EmptyCategoriesMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Category, null, modifier = Modifier.size(60.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No categories yet", color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}