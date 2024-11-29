package dev.sekara.block.mvc.controller

import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.rest.EventDto
import dev.sekara.block.domain.service.BlockingEventService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class WebEventController(
    private val eventService: BlockingEventService
) {

    @GetMapping
    fun getAllEvents(@RequestParam(required = false) limit: Int? = null): List<EventDto> {
        val events = limit?.let { eventService.getLastEvents(it) } ?: eventService.getEvents()
        return events.map { it.toDto() }
    }

    @PostMapping
    fun createEvent(@RequestBody event: EventDto): EventDto {
        return eventService.saveEvent(event.toNewEvent()).toDto()
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
