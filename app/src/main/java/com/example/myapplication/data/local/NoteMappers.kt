package com.example.myapplication.data.local

import com.example.myapplication.data.model.NoteItem

fun NoteEntity.toNoteItem(): NoteItem {
    return NoteItem(
        id = id,
        title = title,
        content = content,
        isFavor = isFavor
    )
}

fun NoteItem.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        isFavor = isFavor
    )
}