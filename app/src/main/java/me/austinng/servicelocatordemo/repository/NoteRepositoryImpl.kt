package me.austinng.servicelocatordemo.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.austinng.servicelocatordemo.data.Note
import me.austinng.servicelocatordemo.local.NoteDao

class NoteRepositoryImpl(
    private val noteDao: NoteDao
): NoteRepository {
    override fun getAllNoteStream(): Flow<List<Note>> {
        return noteDao.getAllNoteStream().map { notes ->
            notes.map { note ->
                Note.fromEntity(note)
            }
        }
    }

    override fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }
}