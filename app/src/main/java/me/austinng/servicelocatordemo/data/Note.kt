package me.austinng.servicelocatordemo.data

import me.austinng.servicelocatordemo.local.NoteEntity

data class Note(
    val id: Long? = null,
    val title: String,
    val content: String
) {
    fun toEntity() = NoteEntity(id, title, content)
    companion object {
        fun fromEntity(noteEntity: NoteEntity) = Note(noteEntity.id, noteEntity.title, noteEntity.content)
    }
}
