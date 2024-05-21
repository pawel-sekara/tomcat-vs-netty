package dev.sekara.block.domain.extension

import dev.sekara.block.domain.entity.Note
import dev.sekara.block.domain.rest.NoteDto

fun NoteDto.toEntity(): Note =
    Note(
        id = id,
        content = content,
        createdAt = createdAt,
    )

fun Note.toDto(): NoteDto = NoteDto(
    id = id,
    content = content,
    createdAt = createdAt,
)
