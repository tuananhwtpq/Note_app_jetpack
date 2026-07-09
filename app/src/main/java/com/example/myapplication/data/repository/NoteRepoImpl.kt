package com.example.myapplication.data.repository

import com.example.myapplication.data.local.NoteDao
import com.example.myapplication.data.local.toNoteEntity
import com.example.myapplication.data.local.toNoteItem
import com.example.myapplication.data.model.NoteItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepoImpl(private val noteDao: NoteDao) : NoteRepository {
    override suspend fun addNote(note: NoteItem) {
        noteDao.insertNote(note.toNoteEntity())
    }

    override suspend fun updateFavorNote(noteId: Long) {
        noteDao.updateFavorNote(noteId)
    }

    override suspend fun updateNote(note: NoteItem) {
        noteDao.updateNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }

    override suspend fun getNoteById(noteId: Long): NoteItem? {
        return noteDao.getNoteById(noteId)?.toNoteItem()
    }

    override fun getAllNotes(): Flow<List<NoteItem>> {

        return noteDao.getAllNotes().map { noteList ->
            noteList.map { note ->
                note.toNoteItem()
            }
        }
    }

}
