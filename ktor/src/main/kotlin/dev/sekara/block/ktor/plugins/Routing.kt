package dev.sekara.block.ktor.plugins

import dev.sekara.block.domain.rest.NoteDto
import dev.sekara.block.ktor.config.Dependencies
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList

fun Application.configureRouting(dependencies: Dependencies) {
    routing {
        val controller = dependencies.notesController

        route("/notes") {
            get {
                val result = call.parameters["limit"]?.let {
                    controller.getLast(it.toInt())
                } ?: controller.getAll()

                call.respond(result.toList())
            }

            put {
                val created = controller.create(call.receive<NoteDto>())
                call.respond(HttpStatusCode.OK, created)
            }
        }
    }
}
