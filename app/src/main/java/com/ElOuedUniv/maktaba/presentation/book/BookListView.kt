package com.ElOuedUniv.maktaba.presentation.book

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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

    // أنميشن تدرج العنوان (Scanline Effect)
    val infiniteTransition = rememberInfiniteTransition(label = "title")
    val xOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1500f,
        animationSpec = infiniteRepeatable(animation = tween(3500, easing = LinearEasing)), label = "offset"
    )

    Scaffold(
        containerColor = GeminiDeepSpace,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "KNOWLEDGE VAULT",
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Cyan, GeminiPurpleNeon, Color.Cyan),
                                    start = androidx.compose.ui.geometry.Offset(xOffset - 500f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(xOffset, 200f)
                                ),
                                fontWeight = FontWeight.Black,
                                letterSpacing = 3.sp,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "${uiState.books.size} Data Fragments Secured",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCategoriesClick) {
                        Icon(Icons.Default.Menu, "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            Box(contentAlignment = Alignment.Center) {
                // هالة ضوئية خلف الزر
                Box(modifier = Modifier.size(65.dp).background(GeminiPurpleNeon.copy(0.3f), CircleShape).blur(15.dp))
                FloatingActionButton(
                    onClick = onAddBookClick,
                    containerColor = GeminiPurpleNeon,
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, "Add Book", modifier = Modifier.size(28.dp))
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // رسم دوائر ضوئية خلفية خفيفة جداً
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(listOf(GeminiPurpleNeon.copy(0.05f), Color.Transparent)),
                    radius = 1000f,
                    center = center
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = GeminiPurpleNeon)
            } else if (uiState.books.isEmpty()) {
                EmptyBooksMessage(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(uiState.books) { index, book ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { visible = true }

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(600, delayMillis = index * 120)) +
                                    slideInVertically(tween(600, delayMillis = index * 120)) { it / 3 }
                        ) {
                            BookVIPItem(book = book, onClick = { onBookClick(book.isbn) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookVIPItem(book: Book, onClick: () -> Unit) {
    val isFinished = book.nbPages > 400

    // ألوان VIP المحدثة: زمردي ملكي للناجح، وأرجواني عميق للقراءة
    val statusColor = if (isFinished) Color(0xFF00FF9D) else Color(0xFFBD00FF)
    val secondaryGlow = if (isFinished) Color(0xFF004D3A) else Color(0xFF3B0054)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. البطاقة الزجاجية مع إضاءة خلفية خفيفة (Ambient Glow)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                // إضاءة خلفية خفيفة جداً تتبع حالة الكتاب
                .background(
                    Brush.verticalGradient(
                        colors = listOf(secondaryGlow.copy(0.15f), Color(0xFF0A0A0E))
                    )
                )
                .border(
                    width = 1.2.dp, // زيادة السماكة قليلاً لإظهار اللمعان
                    brush = Brush.linearGradient(
                        colors = listOf(
                            statusColor.copy(0.4f),
                            Color.Transparent,
                            statusColor.copy(0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = book.title.uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp, // زيادة التباعد للفخامة
                        fontSize = 15.sp,
                        // إضافة ظل خفيف للنص لزيادة الوضوح فوق الزجاج
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = statusColor.copy(0.5f),
                            blurRadius = 8f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // نقطة الحالة مع تأثير "التوهج" (Neon Dot)
                    Box(
                        Modifier
                            .size(6.dp)
                            .background(statusColor, CircleShape)
                            .blur(2.dp) // إضاءة حول النقطة
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "ID: ${book.isbn.takeLast(6)}",
                        color = Color.White.copy(0.4f),
                        fontSize = 10.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // 2. غلاف الكتاب مع إضاءة حواف (Edge Lighting)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.78f)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    translationY = (-10).dp.toPx()
                    shadowElevation = 30f
                    // لون الظل يتبع لون الحالة لإعطاء إضاءة محيطية (Ambient Light)
                    val spotColor = statusColor
                    val ambientColor = statusColor
                    shape = RoundedCornerShape(16.dp)
                    clip = true
                }
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(statusColor.copy(0.5f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // ... (كود AsyncImage والـ Placeholder يبقى كما هو بدون تعديل)
            if (book.imageUrl.isNullOrBlank()) {
                Box(Modifier.fillMaxSize().background(Color(0xFF0F0F15)), contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, null, Modifier.size(50.dp), tint = statusColor.copy(0.2f))
                }
            } else {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // شارة الحالة VIP المحدثة
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Black.copy(0.8f), RoundedCornerShape(8.dp))
                    .border(1.dp, statusColor.copy(0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isFinished) "SECURED" else "ENCRYPTED", // كلمات أكثر فخامة
                    color = statusColor,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(Modifier.size(110.dp).background(GeminiPurpleNeon.copy(0.1f), CircleShape).blur(25.dp))
            Icon(Icons.Default.AutoStories, null, Modifier.size(70.dp), tint = GeminiPurpleNeon.copy(0.3f))
        }
        Spacer(Modifier.height(20.dp))
        Text("ARCHIVE EMPTY", color = Color.White.copy(0.8f), fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Text("No knowledge records found.", color = Color.White.copy(0.4f), fontSize = 12.sp)
    }
}