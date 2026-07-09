package com.example.myapplication.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.NoteRepository
import com.example.myapplication.ui.add_edit.AddEditViewModel
import com.example.myapplication.ui.home.HomeViewModel

class ViewModelFactory(
    private val noteRepository: NoteRepository,
    private val noteId: Long? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(noteRepository) as T
            }

            modelClass.isAssignableFrom(AddEditViewModel::class.java) -> {
                AddEditViewModel(noteRepository, noteId) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}