package me.austinng.servicelocatordemo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAllNoteStream(): Flow<List<NoteEntity>>

    @Insert
    fun insertNote(noteEntity: NoteEntity)
}