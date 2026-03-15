package com.ElOuedUniv.maktaba.presentation.book

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

// الألوان المتوافقة مع الـ BookListView الذي صممناه
private val SurfaceDark = Color(0xFF1E1E24)
private val NeonPurple = Color(0xFF8B5CF6)

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, isbn: String, nbPages: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var nbPages by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        // تغيير شكل ولون النافذة لتناسب التصميم الداكن
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 8.dp,
        title = {
            Text(
                text = "Add New Book",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // تعديل الـ TextField ليكون بتصميم نيون
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = nbPages,
                    onValueChange = { nbPages = it },
                    label = { Text("Number of Pages") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(title, isbn, nbPages.toIntOrNull() ?: 0)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirm", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}