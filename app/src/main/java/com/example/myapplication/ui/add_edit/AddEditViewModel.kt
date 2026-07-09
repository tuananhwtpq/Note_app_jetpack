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

    private val _uiState = MutableStateFlow(
        AddEditUiState(
            noteId = noteId ?: -1L,
            isLoading = noteId != null && noteId != -1L
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        if (noteId != -1L && noteId != null) {
            viewModelScope.launch {
                val currentNote = noteRepository.getNoteById(noteId)
                _uiState.update { currentState ->
                    if (currentNote == null) {
                        currentState.copy(isLoading = false)
                    } else {
                        currentState.copy(
                            noteId = currentNote.id,
                            inputTitle = currentNote.title,
                            inputContent = currentNote.content,
                            isFavor = currentNote.isFavor,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditUiEvent) {
        when (event) {
            AddEditUiEvent.OnSaveNoteClick -> saveNote()
            is AddEditUiEvent.OnInputContentChange -> updateInputContent(event.content)
            is AddEditUiEvent.OnInputTitleChange -> updateInputTitle(event.title)
        }
    }

    private fun updateInputTitle(title: String) {
        _uiState.update { it.copy(inputTitle = title, errorInputTitle = "") }
    }

    private fun updateInputContent(content: String) {
        _uiState.update { it.copy(inputContent = content, errorInputContent = "") }
    }

    private fun saveNote() {
        val title = uiState.value.inputTitle.trim()
        val content = uiState.value.inputContent.trim()

        if (title.isEmpty()) {
            _uiState.update { it.copy(errorInputTitle = "Please enter title") }
            return
        }

        if (content.isEmpty()) {
            _uiState.update { it.copy(errorInputContent = "Please enter content") }
            return
        }

        val noteToSave = NoteItem(
            id = uiState.value.noteId.takeIf { it != -1L } ?: 0L,
            title = title,
            content = content,
            isFavor = uiState.value.isFavor
        )

        viewModelScope.launch {
            if (uiState.value.noteId == -1L) {
                noteRepository.addNote(noteToSave)
            } else {
                noteRepository.updateNote(noteToSave)
            }
            _uiState.update { it.copy(isSaveDone = true) }
        }
    }

}
