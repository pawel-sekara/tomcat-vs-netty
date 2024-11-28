package dev.sekara.block.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.db.EventRepository
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.mapper.toDomain
import dev.sekara.block.domain.mapper.toRecord

class BlockingEventService(
    private val repository: EventRepository,
    private val objectMapper: ObjectMapper,
) {

    fun getEvents() = repository.fetchAll().map { it.toDomain(objectMapper) }

    fun getLastEvents(limit: Int) = repository.fetchLast(limit).map { it.toDomain(objectMapper) }

    fun saveEvent(event: NewEvent) = repository.insert(event.toRecord(objectMapper))?.toDomain(objectMapper)
        ?: throw RuntimeException("Could not save the event")
}

