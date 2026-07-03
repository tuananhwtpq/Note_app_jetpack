package com.example.myapplication.data.repository

import com.example.myapplication.data.model.NoteItem
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: NoteItem)
    suspend fun updateNote(noteId: Long)
    suspend fun deleteNote(noteId: Long)
    fun getAllNotes(): Flow<List<NoteItem>>

}