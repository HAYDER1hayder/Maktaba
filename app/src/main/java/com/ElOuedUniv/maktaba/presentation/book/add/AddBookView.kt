package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ElOuedUniv.maktaba.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookView(
    state: AddBookUiState,
    onAction: (AddBookUiAction) -> Unit,
    onBack: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onAction(AddBookUiAction.OnImageSelected(uri?.toString()))
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onBack()
        }
    }

    Scaffold(
        containerColor = GeminiDeepSpace,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) "EDIT BOOK" else "ADD NEW BOOK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
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

            // --- منطقة غلاف الكتاب ---
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(
                    modifier = Modifier
                        .width(160.dp)
                        .height(230.dp)
                        .shadow(35.dp, RoundedCornerShape(20.dp), spotColor = GeminiPurpleNeon)
                        .clickable { photoPickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(20.dp),
                    color = GeminiGlassCard,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    if (state.imageUrl != null) {
                        AsyncImage(
                            model = state.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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
                            Text("Add Cover", color = GeminiTextSecondary)
                        }
                    }
                }

                if (state.imageUrl != null) {
                    IconButton(
                        onClick = { onAction(AddBookUiAction.OnRemoveImage) },
                        modifier = Modifier
                            .offset(x = 12.dp, y = (-12).dp)
                            .background(Color.Red.copy(0.9f), CircleShape)
                            .size(30.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- حقول الإدخال VIP ---
            VIPInputField(
                value = state.title,
                onValueChange = { onAction(AddBookUiAction.OnTitleChange(it)) },
                label = "Book Title",
                icon = Icons.Default.MenuBook,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ حقل الـ ISBN مع فلتر لمنع الحروف تماماً
            VIPInputField(
                value = state.isbn,
                onValueChange = { onAction(AddBookUiAction.OnIsbnChange(it)) },
                label = "ISBN Number",
                icon = Icons.Default.QrCode,
                enabled = !state.isEditMode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ حقل عدد الصفحات مع فلتر لمنع الحروف تماماً
            VIPInputField(
                value = state.nbPages,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    onAction(AddBookUiAction.OnPagesChange(filteredValue))
                },
                label = "Total Pages",
                icon = Icons.Default.AutoStories,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAction(AddBookUiAction.OnAddClick) }
                )
            )

            Spacer(modifier = Modifier.height(50.dp))

            // --- زر التأكيد (Confirm) ---
            Button(
                onClick = { onAction(AddBookUiAction.OnAddClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(30.dp, RoundedCornerShape(20.dp), spotColor = GeminiPurpleNeon),
                colors = ButtonDefaults.buttonColors(containerColor = GeminiPurpleNeon),
                shape = RoundedCornerShape(20.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = if (state.isEditMode) "UPDATE BOOK" else "CONFIRM ADD",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("CANCEL", color = Color.White.copy(0.5f))
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
    icon: ImageVector,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        label = { Text(label, color = GeminiTextSecondary) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = GeminiPurpleNeon) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GeminiPurpleNeon,
            unfocusedBorderColor = Color.White.copy(0.1f),
            disabledBorderColor = Color.White.copy(0.05f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White.copy(0.5f)
        ),
        shape = RoundedCornerShape(18.dp),
        singleLine = true
    )
}