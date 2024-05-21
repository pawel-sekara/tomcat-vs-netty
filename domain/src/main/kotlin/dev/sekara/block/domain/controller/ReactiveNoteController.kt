package dev.sekara.block.domain.controller

import dev.sekara.block.domain.entity.Note
import dev.sekara.block.domain.extension.toDto
import dev.sekara.block.domain.extension.toEntity
import dev.sekara.block.domain.rest.NoteDto
import dev.sekara.block.domain.service.NoteService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReactiveNoteController(
    private val noteService: NoteService,
) {
    fun getAll(): Flow<NoteDto> = noteService.cGetAll()
        .map(Note::toDto)

    fun getLast(limit: Int): Flow<NoteDto> = noteService.cGetLast(limit)
        .map(Note::toDto)

    suspend fun create(note: NoteDto): NoteDto = noteService.cInsert(note.toEntity())?.toDto()
        ?: throw RuntimeException("Could not create new note with id ${note.id}")
}