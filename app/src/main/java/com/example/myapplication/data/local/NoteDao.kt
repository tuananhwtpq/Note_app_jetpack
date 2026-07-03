package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id = :itemId")
    suspend fun deleteNote(itemId: Long)

    @Query("UPDATE note_table SET isFavor = NOT isFavor WHERE id = :itemId")
    suspend fun updateNote(itemId: Long)
}