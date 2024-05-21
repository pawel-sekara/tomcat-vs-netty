package dev.sekara.block.domain.service

import dev.sekara.block.db.NoteRepository
import dev.sekara.block.db.schema.tables.records.NotesRecord
import dev.sekara.block.domain.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class NoteService(
    private val repository: NoteRepository,
) {

    fun cGetAll(): Flow<Note> = repository.cFetchAll()
        .map(NotesRecord::toDomain)

    fun getAll(): List<Note> = repository.fetchAll()
        .map(NotesRecord::toDomain)

    fun cGetLast(limit: Int) = repository.cFetchLast(limit)
        .map(NotesRecord::toDomain)

    fun getLast(limit: Int) = repository.fetchLast(limit)
        .map(NotesRecord::toDomain)

    fun insert(note: Note): Note? = repository.insert(note.toRecord())?.toDomain()

    suspend fun cInsert(note: Note): Note? = repository.cInsert(note.toRecord())?.toDomain()
}

private fun Note.toRecord(): NotesRecord = NotesRecord(
    id = UUID.randomUUID(),
    note = content,
    createdAt = LocalDateTime.now()
)

private fun NotesRecord.toDomain(): Note {
    return Note(
        id = id,
        content = note,
        createdAt = createdAt.toInstant(ZoneOffset.UTC),
    )
}
