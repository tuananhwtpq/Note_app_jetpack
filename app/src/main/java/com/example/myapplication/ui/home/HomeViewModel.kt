package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllNotes().collect() { listItem ->
                _uiState.update { it.copy(noteList = listItem, isLoading = false) }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnDeleteNoteClick -> deleteNote(event.itemId)
            is HomeUiEvent.OnFavorChange -> changeFavorItem(event.itemId)
            is HomeUiEvent.OnFilterChanged -> changeFilter(event.filter)
        }
    }

    private fun changeFilter(filter: FilterList) {
        _uiState.update { it.copy(currentFilter = filter) }
    }

    private fun changeFavorItem(itemId: Long) {
        viewModelScope.launch {
            repository.updateFavorNote(itemId)
        }
    }

    private fun deleteNote(itemId: Long) {
        viewModelScope.launch {
            repository.deleteNote(itemId)
        }
    }
}
