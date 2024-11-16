package me.austinng.servicelocatordemo.repository

import kotlinx.coroutines.flow.Flow
import me.austinng.servicelocatordemo.data.Note

interface NoteRepository {
    fun getAllNoteStream(): Flow<List<Note>>
    fun insertNote(note: Note)
}