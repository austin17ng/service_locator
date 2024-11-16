package me.austinng.servicelocatordemo

import android.content.Context
import androidx.room.Room
import me.austinng.servicelocatordemo.local.NoteDao
import me.austinng.servicelocatordemo.local.NoteDatabase
import me.austinng.servicelocatordemo.repository.NoteRepository
import me.austinng.servicelocatordemo.repository.NoteRepositoryImpl

object ServiceLocator {
    @Volatile
    private var noteRepository: NoteRepository? = null

    private var noteDatabase: NoteDatabase? = null

    fun provideNoteRepository(context: Context): NoteRepository {
        synchronized(this) {
            return noteRepository ?: NoteRepositoryImpl(provideNoteDao(context))
        }
    }

    private fun provideNoteDao(context: Context): NoteDao {
        return noteDatabase?.noteDao() ?: provideNoteDatabase(context).noteDao()
    }

    private fun provideNoteDatabase(context: Context): NoteDatabase {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "note_database"
        )
            .fallbackToDestructiveMigration()
            .build()
        noteDatabase = instance
        return instance
    }
}