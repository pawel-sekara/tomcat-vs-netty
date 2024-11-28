package dev.sekara.block.webflux.controller

import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.rest.EventDto
import dev.sekara.block.domain.service.ReactiveEventService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull

@RestController("/event")
class WebfluxEventController(
    private val eventService: ReactiveEventService
) {

    @GetMapping
    fun getAllEvents(@RequestParam(required = false) limit: Int? = null): Flow<EventDto> {
        val events = limit?.let { eventService.fetchLastEvents(it) } ?: eventService.fetchEvents()
        return events.map { it.toDto() }
    }

    @PostMapping
    suspend fun createEvent(@RequestBody event: EventDto): EventDto {
        return eventService.saveEvent(event.toNewEvent()).toDto()
    }

    suspend fun getEvents(request: ServerRequest): ServerResponse {
        val flow = getAllEvents(request.queryParamOrNull("limit")?.toInt())
        return ServerResponse.ok().bodyValueAndAwait(flow)
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
