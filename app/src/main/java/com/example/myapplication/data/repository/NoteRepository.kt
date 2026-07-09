package com.example.myapplication.data.repository

import com.example.myapplication.data.model.NoteItem
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: NoteItem)
    suspend fun updateFavorNote(noteId: Long)
    suspend fun updateNote(note: NoteItem)
    suspend fun deleteNote(noteId: Long)
    suspend fun getNoteById(noteId: Long): NoteItem?
    fun getAllNotes(): Flow<List<NoteItem>>

}
