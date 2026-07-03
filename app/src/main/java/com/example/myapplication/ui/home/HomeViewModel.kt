package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.NoteItem
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
            HomeUiEvent.OnAddNoteClick -> addNoteToList()
            is HomeUiEvent.OnDeleteNoteClick -> deleteNote(event.itemId)
            is HomeUiEvent.OnFavorChange -> changeFavorItem(event.itemId)
            is HomeUiEvent.OnFilterChanged -> changeFilter(event.filter)
            is HomeUiEvent.OnInputContentChange -> updateInputContent(event.content)
            is HomeUiEvent.OnInputTitleChange -> updateInputTitle(event.title)
        }
    }

    private fun updateInputTitle(title: String) {
        _uiState.update { it.copy(inputTitle = title) }
    }

    private fun updateInputContent(content: String) {
        _uiState.update { it.copy(inputContent = content) }
    }

    private fun changeFilter(filter: FilterList) {
        _uiState.update { it.copy(currentFilter = filter) }
    }

    private fun changeFavorItem(itemId: Long) {
        viewModelScope.launch {
            repository.updateNote(itemId)
        }
    }

    private fun deleteNote(itemId: Long) {
        viewModelScope.launch {
            repository.deleteNote(itemId)
        }
    }

    private fun addNoteToList() {

        val title = uiState.value.inputTitle
        val content = uiState.value.inputContent

        if (title.isEmpty()) {
            _uiState.update { it.copy(errorInputTitle = "Title cannot empty") }
            return
        }

        if (content.isEmpty()) {
            _uiState.update { it.copy(errorInputContent = "Content cannot empty") }
            return
        }

        val newItem = NoteItem(
            id = 0L,
            title = title,
            content = content,
            isFavor = false
        )

        viewModelScope.launch {
            repository.addNote(newItem)
            _uiState.update {
                it.copy(
                    inputTitle = "",
                    inputContent = "",
                    errorInputTitle = "",
                    errorInputContent = ""
                )
            }
        }
    }


}