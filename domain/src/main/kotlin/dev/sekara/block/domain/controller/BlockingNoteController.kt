package dev.sekara.block.domain.controller

import dev.sekara.block.domain.entity.Note
import dev.sekara.block.domain.extension.toDto
import dev.sekara.block.domain.extension.toEntity
import dev.sekara.block.domain.rest.NoteDto
import dev.sekara.block.domain.service.NoteService

class BlockingNoteController(
    private val noteService: NoteService,
) {

    fun getAll(): List<NoteDto> = noteService.getAll()
        .map(Note::toDto)

    fun getLast(limit: Int): List<NoteDto> = noteService.getLast(limit)
        .map(Note::toDto)

    fun create(note: NoteDto): NoteDto = noteService.insert(note.toEntity())?.toDto()
        ?: throw RuntimeException("Could not create new note with id ${note.id}")
}