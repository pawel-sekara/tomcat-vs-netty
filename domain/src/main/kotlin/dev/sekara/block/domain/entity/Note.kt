package dev.sekara.block.domain.entity

import java.time.Instant
import java.util.UUID

data class Note(
    val id: UUID?,
    val content: String,
    val createdAt: Instant?,
)
