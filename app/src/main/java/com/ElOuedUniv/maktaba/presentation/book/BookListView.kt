package com.ElOuedUniv.maktaba.presentation.book

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                        colors = listOf(GeminiPurpleNeon.copy(0.12f), GeminiDeepSpace),
                        radius = 2800f
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
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
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
    val isFinished = book.nbPages > 400
    val statusColor = if (isFinished) Color(0xFF00FF88) else GeminiPurpleNeon
    val statusText = if (isFinished) "Finished" else "Reading"

    // ✅ التعديل الرئيسي: جعل كامل الـ Column بطاقة واحدة متوهجة ومشعة
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(20.dp), spotColor = statusColor.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1E1E2E)) // لون خلفية البطاقة الموحدة
            .clickable { onClick() }
            .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp)), // حدود نيون خفيفة
        horizontalAlignment = Alignment.Start
    ) {
        // 1. منطقة صورة الكتاب (الجزء العلوي) - لم نعد نضع الـ shadow هنا بل على الـ Column
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.Black.copy(0.2f)) // تظليل خلف الصورة
        ) {
            if (book.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(45.dp),
                        tint = statusColor.copy(0.3f)
                    )
                }
            } else {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 2. ✅ الإطار السفلي للمعلومات (بلون مختلف وتنسيق احترافي داخل البطاقة)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF252538)) // لون الإطار السفلي لتمييز النصوص
                .padding(12.dp)
        ) {
            Text(
                text = book.title,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // معلومات الـ ISBN
                Column {
                    Text("ISBN", color = GeminiTextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Text(
                        book.isbn,
                        color = Color.White.copy(0.7f),
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }

                // علامة الحالة (Status Badge)
                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.5.dp, statusColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = statusColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(80.dp), tint = GeminiPurpleNeon.copy(0.2f))
        Spacer(Modifier.height(16.dp))
        Text("Your vault is empty", color = Color.White, fontWeight = FontWeight.Bold)
    }
}