package dev.sekara.block.domain.rest

import java.time.Instant
import java.util.UUID

data class EventDto(
    val id: UUID?,
    val event: String,
    val data: Map<String, Any>?,
    val createdAt: Instant?,
)
