package com.ElOuedUniv.maktaba.presentation.book

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.launch

// تعريف باليت الألوان النيون
private val DeepDark = Color(0xFF0F0F12)
private val SurfaceDark = Color(0xFF1E1E24)
private val NeonPurple = Color(0xFF8B5CF6)
private val SoftPurple = Color(0xFFC084FC)
private val AccentPink = Color(0xFFEC4899)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(
    onCategoriesClick: () -> Unit = {},
    viewModel: BookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // إظهار نافذة الإضافة بناءً على الحالة
    if (uiState.isAddingBook) {
        AddBookDialog(
            onDismiss = { viewModel.onAction(BookUiAction.OnDismissAddBook) },
            onConfirm = { title, isbn, pages ->
                viewModel.onAction(BookUiAction.OnAddBookConfirm(
                    title = title, isbn = isbn, nbPages = pages
                ))
            }
        )
    }

    Scaffold(
        containerColor = DeepDark,
        topBar = {
            TopAppBar(
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAction(BookUiAction.OnAddBookClick) },
                containerColor = NeonPurple,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = NeonPurple
                )
            } else if (uiState.books.isEmpty()) {
                EmptyBooksMessage(modifier = Modifier.align(Alignment.Center))
            } else {
                BookListContent(
                    books = uiState.books,
                    // سنمرر قيم افتراضية للإحصائيات أو نربطها بالـ UI State لاحقاً
                    totalBooks = uiState.books.size,
                    totalPages = uiState.books.sumOf { it.nbPages }
                )
            }
        }
    }
}

@Composable
fun BookListContent(
    books: List<Book>,
    totalBooks: Int,
    totalPages: Int
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp), // مساحة للزر العائم
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { UltraHeader(totalBooks, totalPages) }
        itemsIndexed(items = books, key = { _, book -> book.isbn }) { _, book ->
            UltraBookItem(book = book)
        }
    }
}

@Composable
fun UltraBookItem(book: Book) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2A2A32)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MenuBook, null, tint = SoftPurple, modifier = Modifier.size(30.dp))
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
                    text = "ISBN: ${book.isbn}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(NeonPurple.copy(0.15f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${book.nbPages} Pages",
                        color = SoftPurple,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun UltraHeader(totalBooks: Int, totalPages: Int) {
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
                Text("Books", color = Color.White.copy(0.8f), style = MaterialTheme.typography.labelMedium)
                Text(totalBooks.toString(), color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            }
            Box(Modifier.width(1.dp).fillMaxHeight(0.5f).background(Color.White.copy(0.3f)))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total Pages", color = Color.White.copy(0.8f), style = MaterialTheme.typography.labelMedium)
                Text(totalPages.toString(), color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
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
        Text("Add your first book to start", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
    }
}