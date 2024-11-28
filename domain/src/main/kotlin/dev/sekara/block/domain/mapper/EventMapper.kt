package dev.sekara.block.domain.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.sekara.block.db.schema.tables.records.EventsRecord
import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

fun NewEvent.toRecord(objectMapper: ObjectMapper): EventsRecord = EventsRecord(
    id = UUID.randomUUID(),
    createdAt = LocalDateTime.now(),
    event = event,
    data = data?.let { objectMapper.writeValueAsString(it) },
)

fun EventsRecord.toDomain(objectMapper: ObjectMapper) = Event(
    id = id,
    createdAt = createdAt.toInstant(ZoneOffset.UTC),
    event = event,
    data = data?.let { objectMapper.readValue<Map<String, Any>>(it) },
)