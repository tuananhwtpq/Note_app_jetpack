package com.example.myapplication.data.model

data class NoteItem(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val isFavor: Boolean = false
)
