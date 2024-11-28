package dev.sekara.block.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import dev.sekara.block.db.EventRepository
import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.mapper.toDomain
import dev.sekara.block.domain.mapper.toRecord
import kotlinx.coroutines.flow.map

class ReactiveEventService(
    private val repository: EventRepository,
    private val objectMapper: ObjectMapper,
) {

    fun fetchEvents() = repository.fetchAllFlow().map { it.toDomain(objectMapper) }

    fun fetchLastEvents(limit: Int) = repository.fetchLastFlow(limit).map { it.toDomain(objectMapper) }

    suspend fun saveEvent(event: NewEvent) = repository.cInsert(event.toRecord(objectMapper)).toDomain(objectMapper)
}