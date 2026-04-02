package com.ElOuedUniv.maktaba.presentation.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(
    onCategoriesClick: () -> Unit = {},
    onAddBookClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    viewModel: BookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = GeminiDeepSpace,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "MY LIBRARY",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "${uiState.books.size} Books in vault",
                            style = MaterialTheme.typography.labelSmall,
                            color = GeminiPurpleNeon.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCategoriesClick) {
                        Icon(Icons.Default.Menu, "Menu", tint = GeminiPurpleNeon)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBookClick,
                containerColor = GeminiPurpleNeon,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.shadow(15.dp, CircleShape, spotColor = GeminiPurpleNeon)
            ) {
                Icon(Icons.Default.Add, "Add Book", modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.radialGradient(
                        colors = listOf(GeminiPurpleNeon.copy(0.15f), GeminiDeepSpace),
                        radius = 2500f
                    )
                )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = GeminiPurpleNeon)
            } else if (uiState.books.isEmpty()) {
                EmptyBooksMessage(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.books) { book ->
                        BookVIPItem(book = book, onClick = { onBookClick(book.isbn) })
                    }
                }
            }
        }
    }
}

@Composable
fun BookVIPItem(book: Book, onClick: () -> Unit) {
    // تحديد الحالة (Reading أو Finished) بناءً على منطق التطبيق
    val isFinished = book.nbPages > 400 // مثال: إذا كان الكتاب طويلاً نعتبره مكتملاً (يمكنك ربطها بـ book.status)
    val statusText = if (isFinished) "Finished" else "Reading"
    val statusColor = if (isFinished) Color(0xFF00FF88) else GeminiPurpleNeon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.Start
    ) {
        // بطاقة صورة الكتاب
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.72f)
                .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = statusColor),
            shape = RoundedCornerShape(16.dp),
            color = GeminiGlassCard,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(0.1f))
        ) {
            if (book.imageUrl.isNullOrBlank()) {
                Box(contentAlignment = Alignment.Center) {
                    Text(book.title.take(1), fontSize = 48.sp, color = statusColor, fontWeight = FontWeight.Black)
                }
            } else {
                AsyncImage(model = book.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
        }

        Spacer(Modifier.height(10.dp))

        // اسم الكتاب
        Text(
            text = book.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        // الـ ISBN والحالة (Status) كما في الصورة المطلوبة
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ISBN:", color = GeminiTextSecondary, fontSize = 9.sp)
                Text(book.isbn, color = Color.White.copy(0.8f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Status:", color = GeminiTextSecondary, fontSize = 9.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(statusText, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = if (isFinished) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp).padding(start = 2.dp),
                        tint = statusColor
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("📚", fontSize = 60.sp)
        Text("The vault is empty", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Add your first masterpiece", color = GeminiTextSecondary, fontSize = 12.sp)
    }
}