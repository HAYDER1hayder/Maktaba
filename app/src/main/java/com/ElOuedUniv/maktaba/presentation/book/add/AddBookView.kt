package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // تأكد من استيراد ألوان النيون التي عرفناها سابقاً
import com.ElOuedUniv.maktaba.presentation.theme.GeminiDeepSpace
import com.ElOuedUniv.maktaba.presentation.theme.GeminiGlassCard
import com.ElOuedUniv.maktaba.presentation.theme.GeminiPurpleNeon
import com.ElOuedUniv.maktaba.presentation.theme.GeminiTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookView(
    state: AddBookUiState,
    onAction: (AddBookUiAction) -> Unit,
    onBack: () -> Unit
) {
    // التنقل التلقائي عند نجاح الإضافة
    if (state.isSuccess) {
        onBack()
    }

    Scaffold(
        containerColor = GeminiDeepSpace, // اللون الأسود المائل للزرقة
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ADD NEW BOOK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- منطقة غلاف الكتاب (كما في الصورة المطلوبة) ---
            Surface(
                modifier = Modifier
                    .width(160.dp)
                    .height(230.dp)
                    .shadow(30.dp, RoundedCornerShape(20.dp), spotColor = GeminiPurpleNeon),
                shape = RoundedCornerShape(20.dp),
                color = GeminiGlassCard,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = GeminiPurpleNeon.copy(alpha = 0.5f)
                    )
                    Text(
                        "Add Cover",
                        style = MaterialTheme.typography.labelMedium,
                        color = GeminiTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- حقول الإدخال ---
            VIPInputField(
                value = state.title,
                onValueChange = { onAction(AddBookUiAction.OnTitleChange(it)) },
                label = "Book Title",
                icon = Icons.Default.MenuBook
            )

            Spacer(modifier = Modifier.height(20.dp))

            VIPInputField(
                value = state.isbn,
                onValueChange = { onAction(AddBookUiAction.OnIsbnChange(it)) },
                label = "ISBN Number",
                icon = Icons.Default.QrCode
            )

            Spacer(modifier = Modifier.height(20.dp))

            VIPInputField(
                value = state.nbPages,
                onValueChange = { onAction(AddBookUiAction.OnPagesChange(it)) },
                label = "Total Pages",
                icon = Icons.Default.AutoStories
            )

            Spacer(modifier = Modifier.height(50.dp))

            // --- زر الحفظ المتوهج (Confirm Add) ---
            Button(
                onClick = { onAction(AddBookUiAction.OnAddClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(25.dp, RoundedCornerShape(18.dp), spotColor = GeminiPurpleNeon),
                colors = ButtonDefaults.buttonColors(containerColor = GeminiPurpleNeon),
                shape = RoundedCornerShape(18.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "CONFIRM ADD",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun VIPInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = GeminiTextSecondary) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = GeminiPurpleNeon) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GeminiPurpleNeon,
            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}
