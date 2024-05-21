package dev.sekara.block.domain.rest

import java.time.Instant
import java.util.UUID

data class NoteDto(
    val id: UUID?,
    val content: String,
    val createdAt: Instant?,
)
