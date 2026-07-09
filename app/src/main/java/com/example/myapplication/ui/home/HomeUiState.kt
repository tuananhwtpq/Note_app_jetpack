package com.example.myapplication.ui.home

import com.example.myapplication.data.model.NoteItem

data class HomeUiState(
    val noteList: List<NoteItem> = emptyList(),
    val currentFilter: FilterList = FilterList.ALL,
    val isLoading: Boolean = true
) {
    val filterList: List<NoteItem>
        get() = when (currentFilter) {
            FilterList.ALL -> noteList
            FilterList.UNFAVOR -> noteList.filter { !it.isFavor }
            FilterList.FAVORITE -> noteList.filter { it.isFavor }
        }
}
