package com.example.myapplication.ui.add_edit

sealed interface AddEditUiEvent {
    data class OnInputTitleChange(val title: String) : AddEditUiEvent
    data class OnInputContentChange(val content: String) : AddEditUiEvent
    object OnAddNote : AddEditUiEvent
    object OnUpdateNote : AddEditUiEvent
}