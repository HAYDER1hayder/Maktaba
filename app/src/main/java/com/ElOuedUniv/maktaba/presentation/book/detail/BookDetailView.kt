package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ElOuedUniv.maktaba.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailView(
    onBackClick: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusColor = uiState.statusColor
    val scrollState = rememberScrollState()

    // أنميشن انسيابي لتحريك الشريط عند دخول الصفحة
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "NeonProgress"
    )

    LaunchedEffect(Unit) {
        viewModel.loadBook()
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is BookDetailUiEvent.NavigateBack -> onBackClick()
                is BookDetailUiEvent.NavigateToEdit -> onNavigateToEdit(event.isbn)
                else -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(GeminiDeepSpace)) {

        AsyncImage(
            model = uiState.book?.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().blur(60.dp).graphicsLayer { alpha = 0.4f },
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color.Transparent, GeminiDeepSpace.copy(alpha = 0.9f), GeminiDeepSpace))
        ))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ARCHIVE", color = Color.White, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, letterSpacing = 6.sp)) },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.onAction(BookDetailUiAction.OnBackClick) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.onAction(BookDetailUiAction.OnEditClick) }) {
                            Icon(Icons.Default.Edit, null, tint = statusColor)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))


                Box(
                    modifier = Modifier
                        .width(210.dp)
                        .aspectRatio(0.7f)
                        .graphicsLayer {
                            translationY = (-scrollState.value * 0.1f)
                            shadowElevation = 60f
                            val spotColor = statusColor
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(24.dp))
                ) {
                    AsyncImage(model = uiState.book?.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }

                Spacer(modifier = Modifier.height(35.dp))


                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
                    Text(
                        text = uiState.book?.title ?: "---",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text("FINISHED", color = statusColor.copy(0.6f), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text("${uiState.pagesRead}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                        }


                        Box(
                            modifier = Modifier
                                .border(1.dp, statusColor.copy(0.2f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("${(uiState.progress * 100).toInt()}%", color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("REMAINING", color = Color.White.copy(0.3f), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text("${uiState.pagesRemaining}", color = Color.White.copy(0.6f), fontSize = 28.sp, fontWeight = FontWeight.Light)
                        }
                    }


                    Box(modifier = Modifier.fillMaxWidth().height(8.dp)) {
                        Box(Modifier.fillMaxSize().background(Color.White.copy(0.05f), CircleShape))
                        Box(
                            Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .background(Brush.horizontalGradient(listOf(statusColor.copy(0.7f), statusColor)), CircleShape)
                                .drawBehind {
                                    drawCircle(
                                        color = statusColor,
                                        radius = 14f,
                                        center = center.copy(x = size.width)
                                    )
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailTile(
                            label = "ISBN CODE",
                            value = uiState.book?.isbn ?: "---",
                            icon = Icons.Default.Fingerprint,
                            color = statusColor,
                            modifier = Modifier.weight(1.3f)
                        )
                        DetailTile(
                            label = "TOTAL PAGES",
                            value = uiState.totalPages.toString(),
                            icon = Icons.Default.AutoStories,
                            color = statusColor,
                            modifier = Modifier.weight(0.7f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun DetailTile(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(0.04f),
        border = BorderStroke(1.dp, Color.White.copy(0.08f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(icon, null, tint = color.copy(0.6f), modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(12.dp))
            Text(label, color = Color.White.copy(0.3f), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}