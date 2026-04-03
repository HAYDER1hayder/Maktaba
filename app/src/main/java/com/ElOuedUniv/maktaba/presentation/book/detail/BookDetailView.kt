package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ElOuedUniv.maktaba.presentation.category.BookDetailUiEvent
import com.ElOuedUniv.maktaba.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailView(
    onBackClick: () -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusColor = uiState.statusColor

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event: BookDetailUiEvent ->
            when (event) {
                is BookDetailUiEvent.NavigateBack -> onBackClick()
            }
        }
    }

    Scaffold(
        containerColor = GeminiDeepSpace,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "BOOK DETAILS",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onAction(BookDetailUiAction.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                // ✅ إضافة زر التعديل في شريط الأدوات العلوي
                actions = {
                    IconButton(onClick = { viewModel.onAction(BookDetailUiAction.OnEditClick) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book",
                            tint = statusColor // استخدام لون النيون للزر
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(
                    Brush.verticalGradient(
                        colors = listOf(statusColor.copy(alpha = 0.08f), GeminiDeepSpace)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // ✅ 1. عرض صورة الكتاب بتوهج نيون (Neon Cover)
            Surface(
                modifier = Modifier
                    .width(220.dp)
                    .aspectRatio(0.7f)
                    .shadow(
                        elevation = 40.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = statusColor
                    ),
                shape = RoundedCornerShape(24.dp),
                color = GeminiGlassCard,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                AsyncImage(
                    model = uiState.book?.imageUrl,
                    contentDescription = "Book Cover",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 2. معلومات التقدم (Reading Progress)
            Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                Text(
                    text = uiState.book?.title ?: "Loading...",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Reading Progress", color = Color.White.copy(0.7f), fontSize = 15.sp)
                    Text(
                        text = "${(uiState.progress * 100).toInt()}%",
                        color = statusColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape)
                        .shadow(10.dp, CircleShape, spotColor = statusColor),
                    color = statusColor,
                    trackColor = Color.White.copy(0.1f)
                )
            }

            Spacer(modifier = Modifier.height(45.dp))

            // 3. إطار المعلومات السفلي
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(20.dp, RoundedCornerShape(28.dp), spotColor = Color.Black),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF1E1E2E),
                border = BorderStroke(1.dp, Color.White.copy(0.08f))
            ) {
                Row(
                    modifier = Modifier.padding(25.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoDetailBox("ISBN", uiState.book?.isbn ?: "---", Icons.Default.Fingerprint, statusColor)
                    InfoDetailBox("Pages", uiState.book?.nbPages.toString(), Icons.Default.AutoStories, statusColor)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun InfoDetailBox(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(color.copy(0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, color = GeminiTextSecondary, fontSize = 11.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}