package dev.sekara.block.webflux.controller

import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.rest.EventDto
import dev.sekara.block.domain.service.ReactiveEventService
import dev.sekara.block.webflux.jpa.JpaEvent
import dev.sekara.block.webflux.jpa.JpaEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.asFlux
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/event")
class WebfluxEventController(
    private val eventService: ReactiveEventService,
    private val eventRepository: JpaEventRepository,
) {

    @GetMapping
    suspend fun getAllEvents(@RequestParam(required = false) limit: Int? = null): Flow<EventDto> {
        val events = limit?.let { eventService.fetchLastEvents(it) } ?: eventService.fetchEvents()
        return events.map { it.toDto() }
    }

    @PostMapping
    suspend fun createEvent(@RequestBody event: EventDto): EventDto {
        return eventService.saveEvent(event.toNewEvent()).toDto()
    }

    @PostMapping("jpa")
    suspend fun createEventJpa(@RequestBody event: JpaEvent): JpaEvent {
        val evt = JpaEvent(
            id = UUID.randomUUID(),
            event = event.event, data = "null", createdAt = Instant.now()
        )
        return eventRepository.insert(evt.id!!, evt.event, evt.data, evt.createdAt!!).awaitSingle()
    }

    @GetMapping("jpa")
    suspend fun getAllEventsJpa(@RequestParam(required = false) limit: Int? = null): Flow<JpaEvent> {
        return eventRepository.findAll().asFlow().apply {
            if (limit != null) {
                take(limit)
            }
        }
    }
}

private fun EventDto.toNewEvent(): NewEvent = NewEvent(
    event = event,
    data = data,
)

private fun Event.toDto() = EventDto(
    id = id,
    event = event,
    data = data,
    createdAt = createdAt
)
