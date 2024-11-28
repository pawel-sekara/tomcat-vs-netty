package dev.sekara.block.ktor

import dev.sekara.block.domain.entity.Event
import dev.sekara.block.domain.entity.NewEvent
import dev.sekara.block.domain.rest.EventDto
import dev.sekara.block.ktor.config.Dependencies
import dev.sekara.block.ktor.plugins.configureMonitoring
import dev.sekara.block.ktor.plugins.addTestRoutes
import dev.sekara.block.ktor.plugins.configureSerialization
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.toList

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dependencies = Dependencies(environment.config)
    configureSerialization(dependencies)
    configureMonitoring()
    addTestRoutes(dependencies)
    addEventRoutes(dependencies)
}

private fun Application.addEventRoutes(dependencies: Dependencies) {
    routing {
        val service = dependencies.eventService

        route("/event") {
            get {
                val result = call.parameters["limit"]?.let { service.fetchLastEvents(it.toInt()) } ?: service.fetchEvents()
                call.respond(result.toList())
            }

            post {
                val created = service.saveEvent(call.receive<EventDto>().toNewEvent())
                call.respond(HttpStatusCode.OK, created.toDto())
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

