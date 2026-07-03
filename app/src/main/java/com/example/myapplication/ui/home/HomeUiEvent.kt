package com.example.myapplication.ui.home

sealed interface HomeUiEvent {
    data class OnInputTitleChange(val title: String) : HomeUiEvent
    data class OnInputContentChange(val content: String) : HomeUiEvent
    object OnAddNoteClick : HomeUiEvent
    data class OnFilterChanged(val filter: FilterList) : HomeUiEvent
    data class OnFavorChange(val itemId: Long) : HomeUiEvent
    data class OnDeleteNoteClick(val itemId: Long) : HomeUiEvent
}