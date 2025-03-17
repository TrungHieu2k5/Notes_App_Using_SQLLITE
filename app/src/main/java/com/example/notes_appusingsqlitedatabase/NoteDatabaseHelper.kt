package com.example.notes_appusingsqlitedatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Notes.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE notes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                content TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun insertNote(title: String, content: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        val result = db.insert("notes", null, values)
        db.close()
        return result
    }

    fun updateNote(id: Int, title: String, content: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        db.update("notes", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllNotes(): List<Notes> {
        val notes = mutableListOf<Notes>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM notes", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            notes.add(Notes(id, title, content))
        }

        cursor.close()
        db.close()
        return notes
    }

    fun deleteNote(id: Int) {
        val db = writableDatabase
        db.delete("notes", "id=?", arrayOf(id.toString()))
        db.close()
    }
}
