package dev.sekara.block.domain.entity

import java.time.Instant
import java.util.UUID

data class Event(
    val id: UUID,
    val event: String,
    val data: Map<String, Any>?,
    val createdAt: Instant,
)