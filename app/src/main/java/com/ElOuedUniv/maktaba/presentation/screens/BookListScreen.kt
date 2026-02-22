package com.ElOuedUniv.maktaba.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel
import kotlin.math.min

// الألوان الخاصة بالتصميم الداكن الحديث
val DeepDark = Color(0xFF0F0F12)
val SurfaceDark = Color(0xFF1E1E24)
val NeonPurple = Color(0xFF8B5CF6)
val SoftPurple = Color(0xFFC084FC)
val AccentPink = Color(0xFFEC4899)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(viewModel: BookViewModel) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        containerColor = DeepDark, // الخلفية الداكنة الأساسية
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Maktaba Library",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepDark,
                    titleContentColor = Color.White
                )
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
            } else {
                if (books.isEmpty()) {
                    EmptyBooksMessage(modifier = Modifier.align(Alignment.Center))
                } else {
                    BookList(
                        books = books,
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BookList(
    books: List<Book>,
    modifier: Modifier = Modifier,
    viewModel: BookViewModel
) {
    val listState = rememberLazyListState()
    val totalBooks by viewModel.countOfBooks.collectAsState(initial = 0)
    val totalPages by viewModel.sumOfPages.collectAsState(initial = 0)

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            UltraHeader(
                totalBooks = totalBooks,
                totalPages = totalPages,
                scrollOffset = listState.firstVisibleItemScrollOffset
            )
        }

        itemsIndexed(
            items = books,
            key = { _, book -> book.isbn } // مفتاح فريد لتحسين الأداء
        ) { index, book ->
            // أنيميشن خفيف جداً لا يسبب تقطيع
            UltraBookItem(book = book)
        }
    }
}

@Composable
fun UltraBookItem(book: Book) {
    // نستخدم التفاعل السطحي بدلاً من الأنيميشن المستمر
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .graphicsLayer {
                // تقليل استهلاك الذاكرة عبر الـ Layer
                clip = true
                shape = RoundedCornerShape(20.dp)
            }
            .clickable { /* Action */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // أيقونة الكتاب
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2A2A32)), // لون ثابت أسرع في الرسم من التدرج
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = SoftPurple,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "ISBN: ${book.isbn}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))


                Box(
                    modifier = Modifier
                        .background(NeonPurple.copy(alpha = 0.15f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${book.nbPages} Pages",
                        color = SoftPurple,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
fun UltraHeader(totalBooks: Int, totalPages: Int, scrollOffset: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(NeonPurple, AccentPink)
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderStatItem("Books", totalBooks.toString(), Modifier.weight(1f))
            Box(modifier = Modifier.width(1.dp).fillMaxHeight(0.5f).background(Color.White.copy(0.3f)))
            HeaderStatItem("Total Pages", totalPages.toString(), Modifier.weight(1f))
        }
    }
}

@Composable
fun HeaderStatItem(label: String, value: String, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(label, color = Color.White.copy(0.8f), style = MaterialTheme.typography.labelMedium)
        AnimatedContent(targetState = value, label = "") { targetValue ->
            Text(
                targetValue,
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black
            )
        }
    }
}


@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoStories,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your library is empty",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Add some books to get started!",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun Modifier.contentSize(size: androidx.compose.ui.unit.Dp, tint: Color): Modifier = this.size(size)




