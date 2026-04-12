package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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

    // استخدام اللون الأخضر النيون الرسمي من واجهتك الرئيسية
    val mainNeonGreen = Color(0xFF00FFD1)

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBack()
    }

    Scaffold(
        containerColor = Color(0xFF0A0A0A),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) "REVISE ARTIFACT" else "INITIALIZE ENTRY",
                        style = TextStyle(fontWeight = FontWeight.Black, letterSpacing = 3.sp, fontSize = 16.sp),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
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
            Spacer(modifier = Modifier.height(10.dp))

            // ✅ منطقة الغلاف (الحجم الضخم مع ظلال نيون خضراء من واجهتك الرئيسية)
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
                    .clickable { photoPickerLauncher.launch("image/*") }
                    // الظل الأخضر الرسمي (Glow)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(mainNeonGreen.copy(alpha = 0.05f), Color.Transparent),
                            radius = 400f
                        )
                    )
                    .border(1.dp, mainNeonGreen.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (state.imageUrl == null) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = mainNeonGreen.copy(alpha = 0.1f)
                    )
                } else {
                    AsyncImage(
                        model = state.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ✅ حشر المدخلات تحت نصف الشاشة كما طلبت
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                OfficialInputField(
                    value = state.title,
                    onValueChange = { onAction(AddBookUiAction.OnTitleChange(it)) },
                    label = "Book Title",
                    icon = Icons.Default.MenuBook,
                    accentColor = mainNeonGreen
                )
                OfficialInputField(
                    value = state.isbn,
                    onValueChange = { onAction(AddBookUiAction.OnIsbnChange(it)) },
                    label = "ISBN Number",
                    icon = Icons.Default.QrCode,
                    enabled = !state.isEditMode,
                    accentColor = GeminiPurpleNeon // استخدام البنفسجي للـ ISBN للتنوع كما في الرئيسية
                )
                OfficialInputField(
                    value = state.nbPages,
                    onValueChange = { onAction(AddBookUiAction.OnPagesChange(it.filter { c -> c.isDigit() })) },
                    label = "Total Pages",
                    icon = Icons.Default.AutoStories,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    accentColor = mainNeonGreen
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ✅ زر التأكيد (صغير، شفاف، ومزاح لليمين مع توهج نيون)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = { onAction(AddBookUiAction.OnAddClick) },
                    modifier = Modifier
                        .height(48.dp)
                        .width(160.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    enabled = state.isButtonEnabled && !state.isLoading,
                    border = BorderStroke(
                        width = 1.dp,
                        // الزر يلمع بالأخضر فقط عندما يكون جاهزاً (Enabled)
                        color = if (state.isButtonEnabled) Color(0xFF00FFD1) else Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = if (state.isEditMode) "UPDATE" else "CONFIRM",
                        style = TextStyle(fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun OfficialInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    accentColor: Color,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(label, color = Color.White.copy(0.5f), fontWeight = FontWeight.Bold) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp)) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color.White.copy(0.1f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = accentColor
        ),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
    )
}