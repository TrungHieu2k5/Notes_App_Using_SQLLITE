package com.example.notes_appusingsqlitedatabase

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// List of colors to be used for note backgrounds
val noteColors = listOf(
    Color(0xFFFF0000), // Red for delete
    Color(0xFFFFB2A5), // Salmon
    Color(0xFFA5FFB2), // Light Green
    Color(0xFFFFF2A5), // Light Yellow
    Color(0xFFA5F2FF), // Light Blue
    Color(0xFFD2A5FF)  // Light Purple
)

@Composable
fun NotesApp(
    context: Context,
    addCreateNote: () -> Unit
) {
    val dbHelper = remember { NoteDatabaseHelper(context) }
    val notes = remember { mutableStateOf(dbHelper.getAllNotes()) }
    var selectedNoteId by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C)) // Darker background matching the image
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF444444)), // Darker gray for buttons
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF444444)), // Darker gray for buttons
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.White
                        )
                    }
                }
            }

            if (notes.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Create your first note !",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedNoteId != null) {
                        item {
                            DeleteNoteItem(
                                onDelete = {
                                    selectedNoteId?.let { id ->
                                        dbHelper.deleteNote(id)
                                        notes.value = dbHelper.getAllNotes()
                                        selectedNoteId = null
                                    }
                                }
                            )
                        }
                    }

                    items(notes.value) { note ->
                        val colorIndex = (note.id % (noteColors.size - 1)) + 1
                        val backgroundColor = noteColors[colorIndex]

                        Box(
                            modifier = Modifier.clickable {
                                selectedNoteId = if (selectedNoteId == note.id) null else note.id
                            }
                        ) {
                            ColorfulNoteItem(
                                note = note,
                                backgroundColor = backgroundColor,
                                isSelected = note.id == selectedNoteId
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { addCreateNote() },
            modifier = Modifier
                .align(if (selectedNoteId == null) Alignment.BottomEnd else Alignment.BottomCenter)
                .padding(bottom = 52.dp, end = 32.dp),
            containerColor = Color(0xFF333333),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DeleteNoteItem(onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFF0000))
            .clickable { onDelete() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun ColorfulNoteItem(note: Notes, backgroundColor: Color, isSelected: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Display just the title for cleaner look, as shown in the image
        Text(
            text = note.title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotesAppPreview() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NotesApp(
            context = context,
            addCreateNote = {}
        )
    }
}