package com.example.notes_appusingsqlitedatabase

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenNotes(
    backScreenHome: () -> Unit,
    context: Context,
    note: Notes? = null // Thêm tham số để nhận dữ liệu ghi chú
) {
    val darkBackground = Color(0xFF121212)
    val darkSecondary = Color(0xFF252525)
    val darkGray = Color(0xFF333333)
    val lightGray = Color(0xFFAAAAAA)
    val accentColor = Color(0xFF4DD0E1)

    val dbHelper = remember { NoteDatabaseHelper(context) }
    // Khởi tạo giá trị từ note nếu có, nếu không thì để mặc định
    var text by remember { mutableStateOf(TextFieldValue(note?.content ?: "")) }
    var title by remember { mutableStateOf(TextFieldValue(note?.title ?: "Title")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(bottom = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(darkGray),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { backScreenHome() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = lightGray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(darkGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "View",
                    tint = lightGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(darkGray),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    if (note == null) {
                        dbHelper.insertNote(title.text, text.text)
                    } else {
                        dbHelper.updateNote(note.id, title.text, text.text)
                    }
                    backScreenHome()
                }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Save",
                        tint = lightGray
                    )
                }
            }
        }

        BasicTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(
                fontSize = 32.sp,
                color = lightGray,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = lightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (text.text.isEmpty()) {
                        Text(
                            text = "Type something...",
                            color = Color(0xFF777777),
                            fontSize = 18.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenNotesPreview() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ScreenNotes(backScreenHome = {}, context = context)
    }
}

@Composable
fun FormatButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String) {
    Icon(
        imageVector = icon,
        contentDescription = description,
        modifier = Modifier.size(24.dp),
        tint = Color(0xFFAAAAAA)
    )
}