package com.ElOuedUniv.maktaba.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

// --- باليت الألوان النيون الاحترافية ---
private val DeepDark = Color(0xFF0F0F12)
private val SurfaceDark = Color(0xFF1E1E24)
private val NeonPurple = Color(0xFF8B5CF6)
private val SoftPurple = Color(0xFFC084FC)
private val AccentPink = Color(0xFFEC4899)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(
    viewModel: BookViewModel,
    onCategoriesClick: () -> Unit = {}
) {
    // ربط البيانات الحالية الخاصة بك
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        containerColor = DeepDark,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Maktaba Library",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            brush = Brush.linearGradient(listOf(Color.White, SoftPurple))
                        )
                    )
                },
                actions = {
                    // الزر الدائري العصري كما في الواجهة المطورة
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f))
                            .clickable { onCategoriesClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Menu, "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepDark)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = NeonPurple
                )
            } else if (books.isEmpty()) {
                EmptyBooksMessage(modifier = Modifier.align(Alignment.Center))
            } else {
                // عرض القائمة مع الإحصائيات (Header)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        StatsHeader(
                            totalBooks = books.size,
                            totalPages = books.sumOf { it.nbPages }
                        )
                    }
                    items(books) { book ->
                        ModernBookItem(book = book)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsHeader(totalBooks: Int, totalPages: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(NeonPurple, AccentPink)))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Books", color = Color.White.copy(0.8f), fontSize = 12.sp)
                Text(totalBooks.toString(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }
            Box(Modifier.width(1.dp).fillMaxHeight(0.5f).background(Color.White.copy(0.3f)))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total Pages", color = Color.White.copy(0.8f), fontSize = 12.sp)
                Text(totalPages.toString(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun ModernBookItem(book: Book) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MenuBook, null, tint = SoftPurple, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "ISBN: ${if(book.isbn.isEmpty()) "---" else book.isbn}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Surface(
                    color = NeonPurple.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${book.nbPages} Pages",
                        color = SoftPurple,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.AutoStories, null, modifier = Modifier.size(60.dp), tint = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text("Your library is empty", color = Color.White, style = MaterialTheme.typography.titleMedium)
        Text("No books found in your database", color = Color.Gray, fontSize = 14.sp)
    }
}