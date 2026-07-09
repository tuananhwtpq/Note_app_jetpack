package com.example.myapplication.ui.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.NoteItem
import com.example.myapplication.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val noteRepository: NoteRepository,
    private val noteId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (noteId != -1L && noteId != null) {
            viewModelScope.launch {
                val currentNote = noteRepository.getNoteById(noteId)
                _uiState.update {
                    it.copy(
                        inputTitle = currentNote.title,
                        inputContent = currentNote.content
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditUiEvent) {
        when (event) {
            AddEditUiEvent.OnAddNote -> addNoteToList()
            is AddEditUiEvent.OnInputContentChange -> updateInputContent(event.content)
            is AddEditUiEvent.OnInputTitleChange -> updateInputTitle(event.title)
            AddEditUiEvent.OnUpdateNote -> updateNoteById()
        }
    }

    private fun updateNoteById() {
        val title = uiState.value.inputTitle
        val content = uiState.value.inputContent

        if (title.isEmpty()) {
            _uiState.update { it.copy(errorInputTitle = "Please enter title") }
            return
        }

        if (content.isEmpty()) {
            _uiState.update { it.copy(errorInputContent = "Please enter content") }
            return
        }

        val updatedNote = NoteItem(
            id = noteId ?: 0L,
            title = title,
            content = content
        )

        viewModelScope.launch {
            noteRepository.updateNote(updatedNote)
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

    private fun updateInputTitle(title: String) {
        _uiState.update { it.copy(inputTitle = title, errorInputTitle = "") }
    }

    private fun updateInputContent(content: String) {
        _uiState.update { it.copy(inputContent = content, errorInputContent = "") }
    }

    private fun addNoteToList() {
        val title = uiState.value.inputTitle
        val content = uiState.value.inputContent

        if (title.isEmpty()) {
            _uiState.update { it.copy(errorInputTitle = "Please enter title") }
            return
        }

        if (content.isEmpty()) {
            _uiState.update { it.copy(errorInputContent = "Please enter content") }
            return
        }

        val newNote = NoteItem(
            title = title,
            content = content,
            isFavor = false
        )

        viewModelScope.launch {
            noteRepository.addNote(newNote)
            _uiState.update {
                it.copy(
                    inputTitle = "",
                    inputContent = "",
                    errorInputTitle = "",
                    errorInputContent = "",
                    isSaveDone = true
                )
            }
        }
    }

}