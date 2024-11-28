package dev.sekara.block.domain.entity

data class NewEvent(
    val event: String,
    val data: Map<String, Any>?,
)