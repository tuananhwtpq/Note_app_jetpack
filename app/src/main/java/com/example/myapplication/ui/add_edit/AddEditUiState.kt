package com.example.myapplication.ui.add_edit

data class AddEditUiState(
    val noteId: Long = -1L,
    val inputTitle: String = "",
    val inputContent: String = "",
    val isFavor: Boolean = false,
    val errorInputTitle: String = "",
    val errorInputContent: String = "",
    val isLoading: Boolean = false,
    val isSaveDone: Boolean = false
)
