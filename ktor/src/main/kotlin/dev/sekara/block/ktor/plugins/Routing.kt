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
        val controller = dependencies.testController

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

        get("/test/cpu-heavy") {
            call.respond(HttpStatusCode.OK, controller.heavy())
        }

        get("/test/cpu-lite") {
            call.respond(HttpStatusCode.OK, controller.lite())
        }

        get("/test/large-string") {
            call.respond(HttpStatusCode.OK, controller.largeString())
        }

        get("/test/large-object") {
            call.respond(HttpStatusCode.OK, controller.largeObject())
        }

        get("/test/block") {
            controller.blockingOp()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/synchronization") {
            controller.blockingIncrement()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/synchronization-lock") {
            controller.lockIncrement()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/synchronization-mutex") {
            controller.mutexIncrement()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/synchronization-context") {
            controller.contextIncrement()
            call.respond(HttpStatusCode.OK)
        }

        get("/test/external-call") {
            controller.externalCall()
            call.respond(HttpStatusCode.OK)
        }
    }
}
