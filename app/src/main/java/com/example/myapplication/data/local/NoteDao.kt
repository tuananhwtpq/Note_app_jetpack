package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id = :itemId")
    suspend fun deleteNote(itemId: Long)

    @Query("SELECT * FROM note_table WHERE id= :noteId")
    fun getNoteById(noteId: Long): NoteEntity


    @Query("UPDATE note_table SET isFavor = NOT isFavor WHERE id = :itemId")
    suspend fun updateFavorNote(itemId: Long)
}