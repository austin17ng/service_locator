package me.austinng.servicelocatordemo.ui.screen.newnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.austinng.servicelocatordemo.data.Note
import me.austinng.servicelocatordemo.repository.NoteRepository

class NewNoteViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {
    private val _note =  MutableStateFlow(Note(title = "", content = ""))
    val note = _note.asStateFlow()

    private val _isNoteSaved = MutableStateFlow(false)
    val isNoteSaved = _isNoteSaved.asStateFlow()

    fun updateTitle(title: String) {
        _note.update { it.copy(title = title) }
    }

    fun updateContent(content: String) {
        _note.update { it.copy(content = content) }
    }

    fun saveNote() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(_note.value)
            _isNoteSaved.value = true
        }
    }
}

class NewNoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewNoteViewModel::class.java)) {
            return NewNoteViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}